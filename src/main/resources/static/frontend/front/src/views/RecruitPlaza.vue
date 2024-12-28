<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import router from "../router";
import {downloadUtils} from "../api/downloadUtils.ts";
import JoinRequestForm from "./JoinRequestForm.vue";
import {useUserStore} from "../stores/userStore.ts";
import { Document, Search } from '@element-plus/icons-vue'
// 响应式变量
const inputLabel = ref('')
const selectedLabels = ref([])
const recruits = ref([])
const selectedRecruits = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const recruitDetailsDialogVisible = ref(false)
const currentRecruits = ref(null)
const joinFormVisible = ref(false)
const userStore = useUserStore();

// 添加标签
const addLabel = () => {
  const label = inputLabel.value.trim()
  if (label && !selectedLabels.value.includes(label)) {
    selectedLabels.value.push(label)
    inputLabel.value = '' // 清空输入
  }
}

// 移除标签
const removeLabel = (index) => {
  selectedLabels.value.splice(index, 1)
}

// 搜索招聘信息
const searchRecruits = async () => {
  try {
    const params = new URLSearchParams()
    selectedLabels.value.forEach((label) => {
      params.append("labels", label)
    })
    params.append("page", currentPage.value.toString())
    params.append("size", pageSize.value.toString())
    const response = await axios.get('/plaza', {
      params,
      headers: {
        Authorization: userStore.token
      }
    })

    if (response.data.code === 1) {
      const result = response.data.data
      if (result && result.length > 0) {
        recruits.value = result.list
        total.value = response.data.total || recruits.value.length
      } else{
        ElMessage.error("无数据")
      }
    } else {
      ElMessage.error('获取招聘信息失败')
    }
  } catch (error) {
    ElMessage.error('网络请求错误')
    console.error(error)
  }
}

// 处理表格多选
const handleSelectionChange = (val) => {
  selectedRecruits.value = val
}

// 处理分页变化
const handlePageChange = (page) => {
  currentPage.value = page
  searchRecruits()
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  return dateTime ? new Date(dateTime).toLocaleString() : ''
}

const goToJoinRequest = () => {
  if (selectedRecruits.value.length === 1) {
    const recruitId = selectedRecruits.value[0].id
    router.push(`/plaza/${recruitId}`)
  }
}

const seeDetail = async (recruit) => {
  currentRecruits.value = recruit
  recruitDetailsDialogVisible.value = true
  try {
    const params = new URLSearchParams()
    params.append("recruitId", recruit.id);
    const res = await axios.get('/plaza/recruit/files', {
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
        currentRecruits.value.files = result.map(item => {
          const decodedData = atob(item.fileData); // 解码 Base64 数据

          // 创建二进制数组
          const byteArray = new Uint8Array(decodedData.length);
          for (let i = 0; i < decodedData.length; i++) {
            byteArray[i] = decodedData.charCodeAt(i);
          }

          const blob = new Blob([byteArray], { type: 'application/octet-stream' })
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
  downloadUtils.generateDownloadLink(file.data, file.name)
}

const goToSubmitJoin = () => {
  joinFormVisible.value = true;
}

const quitDetails = () =>{
  currentRecruits.value = null
  recruitDetailsDialogVisible.value = false
}
</script>

<template>
  <div class="recruit-plaza-container">
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
              @click="searchRecruits"
              class="search-button"
              :icon="Search"
              style="margin-top: 15px"
          >
            搜索
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格区域 -->
    <div class="table-container">
      <el-table
          :data="recruits"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          stripe
          border
          class="custom-table"
      >
        <el-table-column type="selection" width="75" align="center" />

        <el-table-column prop="header" label="标题" min-width="250" align="left">
          <template #default="scope">
            <div class="title-cell">
              <span class="title-text">{{ scope.row.header }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="needNum" label="需求人数" width="120" align="center">
          <template #default="scope">
            <el-badge :value="scope.row.needNum" type="primary" class="number-badge" />
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
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 操作按钮区域 -->
    <div class="actions-container" v-if="selectedRecruits.length > 0">
      <el-button
          type="success"
          @click="goToJoinRequest"
          :disabled="selectedRecruits.length !== 1"
          class="join-button"
      >
        申请加入
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
        v-model="recruitDetailsDialogVisible"
        title="职位详情"
        width="60%"
        class="custom-dialog"
        destroy-on-close
    >
      <template v-if="currentRecruits">
        <el-descriptions :column="2" border class="custom-descriptions">
          <el-descriptions-item label="职位标题" :span="2">
            <span class="description-title">{{currentRecruits.header}}</span>
          </el-descriptions-item>

          <el-descriptions-item label="需求人数">
            <el-tag type="info" effect="plain" class="number-tag">
              {{currentRecruits.needNum}}
            </el-tag>
          </el-descriptions-item>

          <el-descriptions-item label="已有人数">
            <el-tag type="success" effect="plain" class="number-tag">
              {{currentRecruits.hasNum}}
            </el-tag>
          </el-descriptions-item>

          <el-descriptions-item label="申请人数">
            <el-tag type="warning" effect="plain" class="number-tag">
              {{currentRecruits.receiveNum}}
            </el-tag>
          </el-descriptions-item>

          <el-descriptions-item label="职位描述" :span="2">
            <div class="description-content">
              {{currentRecruits.message}}
            </div>
          </el-descriptions-item>

          <el-descriptions-item label="附件" :span="2">
            <div class="attachments-container">
              <template v-if="currentRecruits.files.length > 0">
                <el-button
                    v-for="(file, index) in currentRecruits.files"
                    :key="index"
                    type="primary"
                    link
                    @click="downloadFile(file)"
                    class="file-button"
                >
                  <el-icon class="file-icon"><Document /></el-icon>
                  {{ file.name }}
                </el-button>
              </template>
              <el-empty v-else description="暂无附件" />
            </div>
          </el-descriptions-item>
        </el-descriptions>

        <div class="dialog-footer">
          <el-button type="primary" @click="goToSubmitJoin">提交申请</el-button>
          <el-button @click="quitDetails">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <JoinRequestForm
        v-if="joinFormVisible"
        :recruitID="currentRecruits.id"
        v-model:visible="joinFormVisible"
    />
  </div>
</template>

<style scoped>
.recruit-plaza-container {
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

.tag-0 { background-color: #e6f4ff; color: #1677ff; border-color: #1677ff; }
.tag-1 { background-color: #f6ffed; color: #52c41a; border-color: #52c41a; }
.tag-2 { background-color: #fff7e6; color: #fa8c16; border-color: #fa8c16; }
.tag-3 { background-color: #fff1f0; color: #f5222d; border-color: #f5222d; }
.tag-4 { background-color: #f9f0ff; color: #722ed1; border-color: #722ed1; }

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

.number-badge :deep(.el-badge__content) {
  background-color: #1677ff;
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

.join-button {
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

.number-tag {
  min-width: 60px;
  text-align: center;
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