<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import router from "../router";
import {downloadUtils} from "../api/downloadUtils.ts";
import JoinRequestForm from "./JoinRequestForm.vue";
import {useUserStore} from "../stores/userStore.ts";
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

  <div class="recruit-plaza-container" style="margin-top: 50px;margin-left: 20px">
    <el-form :inline="true" class="search-form">
      <el-form-item label="添加标签" style="margin-bottom: -20px">
        <el-input
            v-model="inputLabel"
            placeholder="输入标签"
            size="small"
            style="width: 350px; margin-left: 10px; height: 35px"
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
        <el-button type="primary" @click="searchRecruits" style="margin-top: 30px">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-table
        :data="recruits"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        stripe
        border
    >
      <el-table-column type="selection" width="75" align="center" />

      <el-table-column prop="header" label="标题" width="439" align="center" />

      <el-table-column prop="needNum" label="需求人数" width="175" align="center" />

      <el-table-column prop="label" label="标签" width="350" header-align="center">

        <template #default="scope">
          <el-tag
              v-for="tag in scope.row.labels"
              :key="tag"
              size="small"
              style="margin-right: 5px;"
          >
            {{ tag }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="创建时间" width="240" align="center">
        <template #default="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>

      <el-table-column label="seeDetail" width="180" align="center">
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
        <el-button @click="goToSubmitJoin">提交申请</el-button>
        <el-button @click="quitDetails">关闭</el-button>
      </template>

    </el-dialog>
    <JoinRequestForm v-if="joinFormVisible" :recruitID="currentRecruits.id" v-model:visible="joinFormVisible"></JoinRequestForm>
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