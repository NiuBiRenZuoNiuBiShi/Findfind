<template>
  <div class="seeker-plaza-container">
    <!-- 搜索区域 -->
    <div class="search-section">
      <el-form :inline="true" class="search-form">
        <el-form-item label="添加标签" class="label-input-item">
          <el-input
              v-model="inputLabel"
              placeholder="输入标签后按回车或点击添加"
              size="default"
              class="label-input"
              @keyup.enter="addLabel"
          >
            <template #append>
              <el-button type="primary" @click="addLabel">
                添加
              </el-button>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item class="tags-container">
          <div class="dynamic-tags">
            <el-tag
                v-for="(tag, index) in selectedLabels"
                :key="tag"
                :class="['custom-tag', `tag-${index % 5}`]"
                closable
                effect="light"
                @close="removeLabel(index)"
            >
              {{ tag }}
            </el-tag>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              @click="searchSeekers"
              class="search-button"
              :icon="Search"
              style="margin-top: 15px"
          >
            搜索
          </el-button>
          <el-button
              type="success"
              @click="goToCreate"
              class="create-button"
              style="margin-top: 15px; margin-left: 10px"
          >
            发布招募
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格区域 -->
    <div class="table-container">
      <el-table
          :data="seekers"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          stripe
          border
          class="custom-table"
      >
        <el-table-column type="selection" width="75" align="center"/>

        <el-table-column prop="header" label="标题" min-width="250" align="left">
          <template #default="scope">
            <div class="title-cell">
              <span class="title-text">{{ scope.row.header }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="position" label="位置" width="180" align="center">
          <template #default="scope">
            <span class="position-text">{{ scope.row.position }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="label" label="标签" min-width="200" align="left">
          <template #default="scope">
            <div class="tags-wrapper">
              <el-tag
                  v-for="(tag, index) in scope.row.labels"
                  :key="tag"
                  :class="['table-tag', `tag-${index % 5}`]"
                  size="small"
              >
                {{ tag }}
              </el-tag>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="180" align="center">
          <template #default="scope">
            <span class="time-text">{{ formatDateTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button
                type="primary"
                link
                @click="seeDetail(scope.row)"
                class="detail-button"
                v-if="inviteFormVisible===false"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 操作按钮区域 -->
    <div class="actions-container" v-if="selectedSeekers.length > 0">
      <el-button
          type="success"
          @click="goToInviteRequest"
          :disabled="selectedSeekers.length !== 1"
          class="invite-button"
      >
        发送邀请
      </el-button>
    </div>

    <!-- 分页区域 -->
    <div class="pagination-wrapper">
      <el-pagination
          background
          layout="prev, pager, next, total"
          :total="total"
          :page-size="pageSize"
          @current-change="handlePageChange"
          class="custom-pagination"
      />
    </div>

    <!-- 详情弹窗 -->
    <el-dialog
        v-model="seekerDetailsDialogVisible"
        title="求职者详情"
        width="60%"
        class="custom-dialog"
        destroy-on-close
    >
      <template v-if="currentSeeker">
        <el-descriptions :column="2" border class="custom-descriptions">
          <el-descriptions-item label="标题" :span="2">
            <span class="description-title">{{ currentSeeker.header }}</span>
          </el-descriptions-item>

          <el-descriptions-item label="位置" :span="2">
            <span class="position-text">{{ currentSeeker.position }}</span>
          </el-descriptions-item>

          <el-descriptions-item label="详细信息" :span="2">
            <div class="description-content">
              {{ currentSeeker.message }}
            </div>
          </el-descriptions-item>

          <el-descriptions-item label="附件" :span="2">
            <div class="attachments-container">
              <template v-if="currentSeeker.files && currentSeeker.files.length > 0">
                <el-button
                    v-for="(file, index) in currentSeeker.files"
                    :key="index"
                    type="primary"
                    link
                    @click="downloadFile(file)"
                    class="file-button"
                >
                  <el-icon class="file-icon">
                    <Document/>
                  </el-icon>
                  {{ file.name }}
                </el-button>
              </template>
              <el-empty v-else description="暂无附件"/>
            </div>
          </el-descriptions-item>
        </el-descriptions>

        <div class="dialog-footer">
          <el-button type="primary" @click="goToInvite">发送邀请</el-button>
          <el-button @click="quitDetails">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <InviteRequestForm
        v-if="inviteFormVisible"
        :seekerID="currentSeeker.id"
        v-model:visible="inviteFormVisible"
    />
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {ElMessage} from 'element-plus'
import axios from 'axios'
import {downloadUtils} from "../api/downloadUtils.ts";
import InviteRequestForm from "./InviteRequestForm.vue";
import {useUserStore} from "../stores/userStore.ts";
import {Document, Search} from '@element-plus/icons-vue'
import router from "../router";


const inputLabel = ref('');
const selectedLabels = ref([]);
const seekers = ref([]);
const selectedSeekers = ref([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);
const seekerDetailsDialogVisible = ref(false);
const currentSeeker = ref({})
const inviteFormVisible = ref(false);
const userStore = useUserStore();

const addLabel = () => {
  const label = inputLabel.value;
  if (label && !selectedLabels.value.includes(label)) {
    selectedLabels.value.push(label);
    inputLabel.value = '';
  }
}

const removeLabel = index => {
  selectedLabels.value.splice(index, 1);
}

const searchSeekers = async () => {
  try {
    const params = new URLSearchParams();
    selectedLabels.value.forEach(item => {
      params.append("labels", item);
    });
    params.append("page", currentPage.value);
    params.append("size", pageSize.value);
    const res = await axios.get('/plaza/seeker', {
      params,
      headers: {
        Authorization: userStore.token
      }
    })
    console.log(res.data)

    if (res.data.code === 1) {
      const result = res.data.data
      if (result.list == null) {
        ElMessage.info("无数据")
        seekers.value = null;
      } else {
        seekers.value = result.list
        total.value = res.data.data.total || seekers.value.length
      }
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
    console.log(error)
  }
}

const handleSelectionChange = (selected) => {
  selectedSeekers.value = selected;
}

const handlePageChange = (page) => {
  currentPage.value = page;
  searchSeekers();
}

const formatDateTime = (dateTime) => {
  return dateTime ? new Date(dateTime).toLocaleDateString() : ' ';
}

const seeDetail = async (seeker) => {
  currentSeeker.value = seeker;
  seekerDetailsDialogVisible.value = true;
  try {
    const params = new URLSearchParams()
    params.append("seekerId", seeker.id);
    const res = await axios.get('/plaza/seeker/files', {
      params,
      headers: {
        Authorization: userStore.token
      }
    })
    console.log(res);
    if (res.data.code === 1) {
      const result = res.data.data
      if (result == null) {
        ElMessage.error("获取文件失败")
      } else {
        currentSeeker.value.files = result.map(item => {
          const decodedData = atob(item.fileData); // 解码 Base64 数据
          // 创建二进制数组
          const byteArray = new Uint8Array(decodedData.length);
          for (let i = 0; i < decodedData.length; i++) {
            byteArray[i] = decodedData.charCodeAt(i);
          }

          const blob = new Blob([byteArray], {type: 'application/octet-stream'})
          return {
            data: blob,
            name: item.fileName,
          }
        })
      }
    }
  } catch (error) {
    console.log(error)
  }
}
const downloadFile = (file) => {
  console.log(file)
  downloadUtils.generateDownloadLink(file.data, file.name);
}
const goToInviteRequest = () => {
  inviteFormVisible.value = true;
}
const goToInvite = () => {
  seekerDetailsDialogVisible.value = false;
  inviteFormVisible.value = true;
}
const quitDetails = () => {
  seekerDetailsDialogVisible.value = false;
  currentSeeker.value = null;
}
const goToCreate = () => {
  router.push("/createSeeker")
}
</script>

<style scoped>
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

.create-button {
  padding: 0 24px;
}
</style>