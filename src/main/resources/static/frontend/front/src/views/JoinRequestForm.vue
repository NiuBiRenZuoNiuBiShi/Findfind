<template>
  <div class="join-request-container">
    <el-card style="margin-top: 175px">
      <template #header>
        <div class="card-header">
          <span>申请加入招聘</span>
        </div>
      </template>

      <el-form ref="joinRequestFormRef" :model="joinRequestForm" label-width="120px">
        <el-form-item label="申请消息" prop="message">
          <el-input
              v-model="joinRequestForm.message"
              type="textarea"
              placeholder="请输入申请消息"
              :rows="4"
          />
        </el-form-item>

        <el-form-item label="上传文件" style="margin-top: 20px;margin-bottom: -5px">
          <el-upload
              v-model:file-list="fileList"
              action=""
              :auto-upload="false"
              multiple
              :limit="5"
              :on-exceed="handleExceed"
          >
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">
                最多上传5个文件，单个文件不超过10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitJoinRequest">提交申请</el-button>
          <el-button @click="cancelRequest">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const recruitId = ref(null)
const joinRequestForm = ref({
  message: ''
})
const fileList = ref([])

onMounted(() => {
  // 从路由获取招聘ID
  recruitId.value = route.params.recruitId
})

// 处理文件数量超出
const handleExceed = () => {
  ElMessage.warning('最多只能上传5个文件')
}

// 提交加入请求
const submitJoinRequest = async () => {
  if (!joinRequestForm.value.message.trim()) {
    ElMessage.error('请填写申请消息')
    return
  }

  // 创建FormData
  const formData = new FormData()

  // 添加文本消息
  formData.append('message', joinRequestForm.value.message)

  // 添加文件
  fileList.value.forEach((file) => {
    formData.append('files', file.raw)
  })

  try {
    const response = await axios.post(`/plaza/${recruitId.value}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.data.code === 1) {
      ElMessage.success('申请提交成功')
      await router.push('/plaza')
    } else {
      ElMessage.error('申请提交失败')
    }
  } catch (error) {
    ElMessage.error('网络请求错误')
    console.error(error)
  }
}

// 取消申请
const cancelRequest = () => {
  router.push('/plaza')
}
</script>

<style scoped>
.join-request-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>