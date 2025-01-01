<script lang="ts" setup>
import {ref, onMounted} from 'vue';
import {getUserList} from '../api/getUserList';
import {ElMessage} from 'element-plus';
import InviteRequestForm from "./InviteRequestForm.vue";
import {instance} from "../api/user.ts";

interface User {
  username: string;
  nickName: string;
  id: string;
  email: string;
  phone: string;
  biology: string;
  position: string;
  status: string;
  createTime: string;
  updateTime: string;
}

interface PageInfo<T> {
  total: number;
  list: T[];
  pageNum: number;
  pageSize: number;
  size: number;
}

const DialogVisible = ref(false);
const tableData = ref<PageInfo<User> | null>(null);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const inviteFormVisible = ref(false);
const seekID = ref();
const goToInviteRequest = async (seekerID: string) => {
  console.log('Seeker ID:', seekerID);
  const token = localStorage.getItem('token');
  const userAll = await instance.get('/user/userInfo', {
    headers: {
      Authorization: token
    }
  });
  const currentUserId = userAll.data.data.id;
  console.log('Current user ID:', currentUserId);
  if (seekerID === currentUserId) {
    ElMessage.error('不能邀请自己');
    return;
  }
  seekID.value = seekerID;
  DialogVisible.value = true;
  inviteFormVisible.value = true;
};
const fetchUsers = async () => {
  try {
    const res = await getUserList(currentPage.value, pageSize.value);
    console.log('Raw response:', res); // 打印出完整的响应对象
    if (res && res.data.data && 'list' in res.data.data && Array.isArray(res.data.data.list)) {
      tableData.value = res.data.data as PageInfo<User>;
      total.value = res.data.data.total;
    } else {
      console.error('API response is not a PageInfo object:', res.data);
    }
  } catch (error) {
    console.error('Failed to fetch user list:', error);
    ElMessage.error('获取用户列表失败');
  }
};

onMounted(() => {
  fetchUsers();
});

const handlePageChange = (newPage: number) => {
  currentPage.value = newPage;
  fetchUsers();
};
</script>
<template>
  <div class="common-layout">
    <el-container>
      <el-header></el-header>
      <el-main>
        <el-table :data="tableData?.list" stripe style="width: 100%">
          <el-table-column prop="id" label="ID" width="100" style="height: 150px;">
            <template #default="scope">
              {{ scope.row.id || 'null' }}
            </template>
          </el-table-column>
          <el-table-column prop="username" label="姓名" width="180">
            <template #default="scope">
              {{ scope.row.username || 'null' }}
            </template>
          </el-table-column>
          <el-table-column prop="nickName" label="昵称" width="180" style="height: 150px;">
            <template #default="scope">
              {{ scope.row.nickName || 'null' }}
            </template>
          </el-table-column>
          <el-table-column prop="position" label="地址" width="180" style="height: 150px;">
            <template #default="scope">
              {{ scope.row.position || 'null' }}
            </template>
          </el-table-column>
          <el-table-column prop="phone" label="电话" width="180" style="height: 150px;">
            <template #default="scope">
              {{ scope.row.phone || 'null' }}
            </template>
          </el-table-column>
          <el-table-column prop="biology" label="自我介绍" style="height: 150px;">
            <template #default="scope">
              {{ scope.row.biology || 'null' }}
            </template>
          </el-table-column>
          <el-table-column prop="position" label="地址" width="180" style="height: 150px;">
            <template #default="scope">
              {{ scope.row.position || 'null' }}
            </template>
          </el-table-column>
          <el-table-column prop="username">
            <template #default="scope">
              <el-button
                  @click="goToInviteRequest(scope.row.id)"
                  class="invite-button"
              >
                发送邀请
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog
            v-model="DialogVisible"
            title="邀请"
            width="60%"
            class="custom-dialog"
            destroy-on-close
            v-if="inviteFormVisible"
        >
          <InviteRequestForm
              v-if="inviteFormVisible" :seekerID="seekID"
              v-model:visible="inviteFormVisible"
          />
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
      </el-main>
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

.seeker-plaza-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 48px);
  margin-top: 64px;
}

.search-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  margin-bottom: 24px;
}

.label-input-item {
  margin-bottom: 0;
}

.label-input {
  width: 400px;
}

.label-input :deep(.el-input__wrapper) {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.dynamic-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 8px 0;
}

.custom-tag {
  padding: 0 12px;
  height: 32px;
  line-height: 30px;
  border-radius: 16px;
  font-weight: 500;
}

.tag-0 {
  background-color: #e6f4ff;
  color: #1677ff;
  border-color: #1677ff;
}

.tag-1 {
  background-color: #f6ffed;
  color: #52c41a;
  border-color: #52c41a;
}

.tag-2 {
  background-color: #fff7e6;
  color: #fa8c16;
  border-color: #fa8c16;
}

.tag-3 {
  background-color: #fff1f0;
  color: #f5222d;
  border-color: #f5222d;
}

.tag-4 {
  background-color: #f9f0ff;
  color: #722ed1;
  border-color: #722ed1;
}

.search-button {
  margin-left: 16px;
  padding: 0 24px;
}

.table-container {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  margin-bottom: 24px;
}

.custom-table {
  margin: 16px 0;
}

.title-cell {
  padding: 8px 0;
}

.title-text {
  font-weight: 500;
  color: #2c3e50;
}

.position-text {
  color: #666;
  font-size: 14px;
}

.tags-wrapper {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.table-tag {
  border-radius: 12px;
}

.time-text {
  color: #666;
  font-size: 14px;
}

.detail-button {
  font-weight: 500;
}

.actions-container {
  margin: 24px 0;
  display: flex;
  justify-content: center;
}

.invite-button {
  padding: 0 32px;
  height: 40px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.custom-pagination {
  padding: 16px 0;
}

.custom-dialog :deep(.el-dialog__body) {
  padding: 24px;
}

.description-title {
  font-size: 16px;
  font-weight: 500;
  color: #2c3e50;
}

.description-content {
  white-space: pre-wrap;
  line-height: 1.6;
  color: #666;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 4px;
}

.attachments-container {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding: 12px;
}

.file-button {
  display: flex;
  align-items: center;
  gap: 8px;
}

.file-icon {
  font-size: 16px;
}

.dialog-footer {
  margin-top: 24px;
  text-align: right;
}
</style>