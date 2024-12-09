<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import router from "../router";
import {downloadUtils} from "../api/downloadUtils.ts";

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
    const response = await axios.get('/plaza', {
      params: {
        labels: selectedLabels.value,
        page: currentPage.value,
        size: pageSize.value
      }
    })

    if (response.data.code === 1) {
      recruits.value = response.data.data
      total.value = response.data.total || recruits.value.length
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

const seeDetail = (recruit) => {
  currentRecruits.value = recruit
  recruitDetailsDialogVisible.value = true
}

const downloadFile = (file) => {
  downloadUtils.generateDownloadLink(file.data, file.name)
}
</script>

<template>

  <div class="recruit-plaza-container" style="margin-top: 70px;margin-left: 20px">
    <el-form :inline="true" class="search-form">
      <el-form-item label="添加标签">
        <el-input
            v-model="inputLabel"
            placeholder="输入标签"
            size="small"
            style="width: 200px;"
            @keyup.enter="addLabel"
        >
          <template #append>
            <el-button @click="addLabel">添加</el-button>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item>
        <div class="dynamic-tags">
          <el-tag
              v-for="(tag, index) in selectedLabels"
              :key="tag"
              closable
              @close="removeLabel(index)"
          >
            {{ tag }}
          </el-tag>
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="searchRecruits">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-table
        :data="recruits"
        style="width: 100%"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />

      <el-table-column prop="header" label="标题" width="180" />

      <el-table-column prop="needNum" label="需求人数" width="100" />

      <el-table-column prop="label" label="标签" width="200">

        <template #default="scope">
          <el-tag
              v-for="tag in scope.row.label"
              :key="tag"
              size="small"
              style="margin-right: 5px;"
          >
            {{ tag }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>

      <el-table-column label="seeDetail" width="100">
        <template #default="scope">
          <el-button
              type="text"
              size="small"
              @click="seeDetail(scope.row)"
          >
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>


    <div class="actions-container" v-if="selectedRecruits.length > 0">
      <el-button
          type="success"
          @click="goToJoinRequest"
          :disabled="selectedRecruits.length !== 1"
      >
        申请加入
      </el-button>
    </div>

    <el-pagination
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        @current-change="handlePageChange"
        class="pagination"
    />

    <el-dialog
      v-model="recruitDetailsDialogVisible"
      title="RecruitDetails"
      width="50%"
    >
      <template v-if="currentRecruits">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Title" :span="2">
            {{currentRecruits.header}}
          </el-descriptions-item>
            <!-- 三个数字 -->
            <el-descriptions-item label="NeedNum">
              {{currentRecruits.needNum}}
            </el-descriptions-item>
            <el-descriptions-item label="HasNum">
              {{currentRecruits.hasNum}}
            </el-descriptions-item>
            <el-descriptions-item label="ReceiveNum">
              {{currentRecruits.receiveNum}}
            </el-descriptions-item>

            <!-- 大段文本 -->
            <el-descriptions-item label="Message" :span="2">
              {{currentRecruits.message}}
            </el-descriptions-item>

            <!-- 附件下载 -->
          <el-descriptions-item label="Attachments" :span="2">
            <ul v-if="currentRecruits.files.length > 0">
              <li v-for="(file, index) in currentRecruits.files" :key="index">
                <button @click="downloadFile(file)">
                  {{ file.name }}
                </button>
              </li>
            </ul>
            <p v-else>暂无附件</p>
          </el-descriptions-item>
        </el-descriptions>
      </template>

    </el-dialog>
  </div>
</template>

<style scoped>
.recruit-plaza-container {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.dynamic-tags {
  display: flex;
  gap: 10px;
  margin-left: 10px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>