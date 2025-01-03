<script lang="ts" setup>
import {ref, onMounted} from 'vue';
import {getUserInvites} from '../api/getUserInvites.ts';
import {ElMessage} from 'element-plus';
import {getTeamInfo as apiGetTeamInfo} from "../api/getTeamInfo.ts";
import {updateInvite} from "../api/updateInvite.ts";
import {getUserInfoById} from "../api/getUserInfo.ts";

interface InviteRequest {
  id: number;
  userID: number;
  teamID: number;
  releaserID: number;
  header: string;
  message: string;
  response: string;
  status: number; // 0:未处理 1:已同意 2:已拒绝
  createTime: string;
  updateTime: string;
  teamName?: string; // 队伍名称
  teamDescription?: string; // 队伍描述
  releaserName?: string; // 发布者名称
}

interface PageInfo<T> {
  total: number;
  list: T[];
  pageNum: number;
  pageSize: number;
  size: number;
}

const tableData = ref<PageInfo<InviteRequest> | null>(null);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const dialogVisible: ref<boolean> = ref(false);
const form: ref<{ response: string; status: number; requestId: number }> = ref({response: '', status: 0, requestId: 0});
const currentRow: ref<InviteRequest | null> = ref(null);
const token = localStorage.getItem('token');
// 根据队伍ID获取队伍信息
const getTeamInfo = async (teamId: number) => {
  try {
    const response = await apiGetTeamInfo(teamId);
    console.log('Raw response3:', response); // 打印出完整的
    if (response && response.data.data) {
      console.log('Raw response:', response.data.data); // 打印出完整的响应对象
      return response.data.data; // 确保后端返回的数据结构中包含name和description字段
    } else {
      ElMessage.error('获取队伍信息失败');
      return null;
    }
  } catch (error) {
    console.error('Failed to fetch team info:', error);
    ElMessage.error('获取队伍信息失败');
    return null;
  }
};
const fetchInvites = async () => {
  try {
    const res = await getUserInvites(currentPage.value, pageSize.value);
    console.log('Raw response:', res); // 打印出完整的响应对象
    if (res && res.data.data && 'list' in res.data.data && Array.isArray(res.data.data.list)) {
      tableData.value = res.data.data as PageInfo<InviteRequest>;
      total.value = res.data.data.total;
      await Promise.all(tableData.value.list.map(async (invite) => {
        const teamInfo = await getTeamInfo(invite.teamID);
        invite.releaserName = await getUserName(invite.releaserID)
        if (teamInfo) {
          console.log('Raw response2:', teamInfo); // 打印出完整的响应对象
          invite.teamName = teamInfo.name;
          invite.teamDescription = teamInfo.description;
        } else
          console.log('no'); // 打印出完整的响应对象
      }));
    } else {
      console.error('API response is not a PageInfo object:', res.data);
    }
  } catch (error) {
    console.error('Failed to fetch user list:', error);
    ElMessage.error('获取数据失败');
  }
};
onMounted(() => {
  fetchInvites();
});
const handlePageChange = (newPage: number) => {
  currentPage.value = newPage;
  fetchInvites();
};
const handleOpenDialog = (row: InviteRequest) => {
  currentRow.value = row;
  form.value.requestId = row.id;
  form.value.response = row.response;
  form.value.status = row.status;
  dialogVisible.value = true;
};
const handleDialogSubmit = async () => {
  console.log(form.value.response);
  if (currentRow.value) {
    // 构建请求体
    const requestData = {
      requestId: form.value.requestId,
      status: form.value.status,
      response: form.value.response.trim() // 去除回复文本两端的空白字符
    };
    try {
      console.log(requestData.requestId, requestData.status, requestData.response);
      const res = await updateInvite(requestData.requestId, requestData.status, requestData.response);
      console.log(res);
      if (res.data.code === 1) {
        ElMessage.success(res.data.message);
        setTimeout(() => {
          window.location.reload(); // 强制刷新页面
        }, 1000);
      } else {
        ElMessage.error(res.data.message);
      }
    } catch (error) {
      console.error('Failed to handle invite request:', error);
      ElMessage.error('处理请求失败');
    }
    // 关闭弹窗
    dialogVisible.value = false;
  }
};
const getUserName = async (userId: number) => {
  const res = await getUserInfoById(userId)
  console.log(res.data.data.username)
  return res.data.data.username
}
</script>
<template>
  <div class="common-layout">
    <el-container>
      <el-header></el-header>
      <el-main>
        <el-table :data="tableData?.list" stripe style="width: 100%">
          <el-table-column prop="releaserName" label="邀请人" width="100" style="height: 150px;">
            <template #default="scope">
              {{ scope.row.releaserName || 'null' }}
            </template>
          </el-table-column>
          <el-table-column prop="teamID" label="队伍id" width="100" style="height: 150px;">
            <template #default="scope">
              {{ scope.row.teamID || 'null' }}
            </template>
          </el-table-column>
          <el-table-column prop="teamName" label="队伍名称" width="180">
            <template #default="scope">
              {{ scope.row.teamName || '未知队伍' }}
            </template>
          </el-table-column>
          <el-table-column prop="teamDescription" label="队伍描述" width="280">
            <template #default="scope">
              {{ scope.row.teamDescription || '无描述' }}
            </template>
          </el-table-column>
          <el-table-column prop="message" label="内容" width="280">
            <template #default="scope">
              {{ scope.row.message || 'null' }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="处理结果" width="180">
            <template #default="scope">
                               <span :class="'status-box status-' + scope.row.status">
                                     {{
                                   scope.row.status === 0 ? '未处理' : scope.row.status === 1 ? '已同意' : scope.row.status === 2 ? '已拒绝' : 'null'
                                 }}
                                </span>
            </template>
          </el-table-column>
          <el-table-column prop="response" label="回复" style="height: 150px;">
            <template #default="scope">
              {{ scope.row.response || 'null' }}
            </template>
          </el-table-column>
          <!-- 按钮列 -->
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <!-- 只有当 status 为 0 时，才显示按钮 -->
              <el-button v-if="scope.row.status === 0" @click="handleOpenDialog(scope.row)">处理</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-dialog
          v-model="dialogVisible"
          title="处理请求"
          width="60%"
      >
        <el-form>
          <el-form-item label="回复">
            <el-input v-model="form.response" type="textarea" :autosize="{minRows:10}"></el-input>
          </el-form-item>
          <el-form-item label="选择">
            <el-radio-group v-model="form.status">
              <el-radio value=0>再想想</el-radio>
              <el-radio value=1>接受</el-radio>
              <el-radio value=2>拒绝</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <span slot="footer">
                          <el-button @click="dialogVisible =false">取消</el-button>
                         <el-button type="primary" @click="handleDialogSubmit">提交</el-button>
                       </span>
      </el-dialog>
    </el-container>
    <el-pagination
        v-if="total > 0"
        layout="total, prev, pager, next"
        :total="total"
        :current-page="currentPage"
        :page-size="pageSize"
        @current-change="handlePageChange"
        class="pagination">
    </el-pagination>
  </div>
</template>
<style scoped>
.common-layout {
  padding: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.status-box {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  color: #fff; /* 文字颜色 */
  font-weight: bold;
}

/* 定义不同状态的颜色 */
.status-0 {
  background-color: #f56c6c; /* 未处理，红色 */
}

.status-1 {
  background-color: #67c23a; /* 已同意，绿色 */
}

.status-2 {
  background-color: #e6a23c; /* 已拒绝，橙色 */
}
</style>