<script lang="ts" setup>
import {ref, onMounted} from 'vue';
import {getAllOtherUserJoins, getUserJoins, updateUserJoin} from '../api/getUserJoins.ts';
import {getTeamInfo as apiGetTeamInfo} from "../api/getTeamInfo.ts";
import {ElMessage} from 'element-plus';
import {getTeamFromUser} from "../api/getTeamList.ts";
import {handleJoinRequest} from "../api/joinRequest.ts";
import {getUserInfoById} from "../api/getUserInfo.ts";

interface JoinRequest {
  id: number;
  userId: number;
  teamId: number;
  message: string;
  response: string;
  status: number; // 0:未处理 1:已同意 2:已拒绝
  createTime: string;
  updateTime: string;
  teamName?: string; // 队伍名称
  teamDescription?: string; // 队伍描述
  userName?: string; // 用户名
}

interface PageInfo<T> {
  total: number;
  list: T[];
  pageNum: number;
  pageSize: number;
  size: number;
}

const tableData = ref<PageInfo<JoinRequest> | null>(null);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
//处理用户通过其他方式加入队伍
const changeJoinStates = async () => {
  //判断用户是否已经加入队伍
  const teamBelongUser = await getTeamFromUser()
  const res = await getUserJoins(currentPage.value, pageSize.value);
  for (let i = 0; i < res.data.data.list.length; i++) {
    for (let j = 0; j < teamBelongUser.data.data.length; j++) {
      if (res.data.data.list[i].teamId === teamBelongUser.data.data[j].id) {
        res.data.data.list[i].status = 1
        console.log(res.data.data.list[i].teamId)
        await updateUserJoin(res.data.data.list[i].id)
      }
    }
  }
}
changeJoinStates();
// 根据队伍ID获取队伍信息
const getTeamInfo = async (teamId: number) => {
  try {
    const response = await apiGetTeamInfo(teamId);
    console.log('Raw response3:', response); // 打印出完整的响应对象
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

const fetchJoins = async () => {
  try {
    const res = await getAllOtherUserJoins(currentPage.value, pageSize.value);
    console.log('Raw response:', res); // 打印出完整的响应对象
    if (res && res.data.data && 'list' in res.data.data && Array.isArray(res.data.data.list)) {
      tableData.value = res.data.data as PageInfo<JoinRequest>;
      total.value = res.data.data.total;
      await Promise.all(tableData.value.list.map(async (join) => {
        join.userName = await getUserName(join.userId)
        const teamInfo = await getTeamInfo(join.teamId);
        if (teamInfo) {
          join.teamName = teamInfo.name;
          join.teamDescription = teamInfo.description;
        }
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
  fetchJoins();
});

const handlePageChange = (newPage: number) => {
  currentPage.value = newPage;
  fetchJoins();
};
const dialogVisible = ref(false);
const form = ref({response: '', status: 0, requestId: 0});
const currentRow = ref<JoinRequest | null>(null);
const handleOpenDialog = (row: JoinRequest) => {
  form.value.requestId = row.id;
  form.value.response = row.response;
  form.value.status = row.status;
  dialogVisible.value = true;
  currentRow.value = row;
};
const handleDialogSubmit = async () => {
  if (currentRow.value) {
    // 构建请求体
    const requestData = {
      requestId: form.value.requestId,
      status: form.value.status,
      response: form.value.response.trim() // 去除回复文本两端的空白字符
    };
    try {
      console.log(requestData.requestId, requestData.status, requestData.response);
      const res = await handleJoinRequest(requestData.requestId, requestData.status, requestData.response);
      console.log(res);
      if (res.data.code === 1) {
        ElMessage.success(res.data.message);
        setTimeout(() => {
          window.location.reload(); // 强制刷新页面
        }, 1000);
      } else if (res.data.message === '参数错误') {
        ElMessage.success("回复内容已保存");
        setTimeout(() => {
          window.location.reload(); // 强制刷新页面
        }, 1000);
      } else {
        ElMessage.error(res.data.message);
      }
    } catch (error) {
      console.error('Failed to handle join request:', error);
      ElMessage.error('处理请求失败');
    }
    // 关闭弹窗
    dialogVisible.value = false;
  }
};
const closeDia = () => {
  form.value.response = '';
  dialogVisible.value = false;
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
          <el-table-column prop="teamId" label="队伍id" width="200"></el-table-column>
          <el-table-column prop="teamName" label="队伍名称" width="180">
            <template #default="scope">
              {{ scope.row.teamName || '未知队伍' }}
            </template>
          </el-table-column>
          <el-table-column prop="userName" label="申请用户" width="280">
            <template #default="scope">
              {{ scope.row.userName || 'error' }}
            </template>
          </el-table-column>
          <el-table-column prop="message" label="内容" width="280"></el-table-column>
          <el-table-column prop="status" label="处理结果" width="180">
            <template #default="scope">
                            <span :class="'status-box status-' + scope.row.status">
                                {{
                                scope.row.status === 0 ? '未处理' : scope.row.status === 1 ? '已同意' : scope.row.status === 2 ? '已拒绝' : 'null'
                              }}
                            </span>
            </template>
          </el-table-column>
          <el-table-column prop="response" label="回复"></el-table-column>
          <!-- 其他列 -->
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
                          <el-button @click="closeDia">取消</el-button>
                         <el-button type="primary" @click="handleDialogSubmit">提交</el-button>
                       </span>
      </el-dialog>
      <el-pagination
          v-if="total > 0"
          layout="total, prev, pager, next"
          :total="total"
          :current-page="currentPage"
          :page-size="pageSize"
          @current-change="handlePageChange"
          class="pagination">
      </el-pagination>
    </el-container>
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

/* 定义一个 CSS 类，用于设置带颜色的方框 */
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