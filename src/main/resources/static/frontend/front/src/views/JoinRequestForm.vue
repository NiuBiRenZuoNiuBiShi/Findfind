<template>
  <div class="join-request-container">
    <div class="request-form-wrapper">
      <div class="form-header">
        <h2>申请加入招聘</h2>
      </div>

      <el-form
          ref="joinRequestFormRef"
          :model="joinRequestForm"
          label-width="120px"
          class="request-form"
      >
        <el-form-item label="申请消息" prop="message">
          <el-input
              v-model="joinRequestForm.message"
              type="textarea"
              placeholder="请输入申请消息"
              :rows="4"
              class="custom-textarea"
          />
        </el-form-item>

        <el-form-item label="上传文件">
          <el-upload
              v-model:file-list="fileList"
              action=""
              :auto-upload="false"
              multiple
              :limit="5"
              :on-exceed="handleExceed"
              class="upload-section"
          >
            <el-button type="primary" class="upload-button">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">
                最多上传5个文件，单个文件不超过10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item class="dialog-footer">
          <el-button type="primary" @click="submitJoinRequest" class="submit-btn">提交申请</el-button>
          <el-button @click="exit" type="danger" class="cancel-btn">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref} from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import {useUserStore} from "../stores/userStore.ts";

const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const props = defineProps({
  recruitID: Number,
  visible: Boolean,
})
const emit = defineEmits(['update:visible'])

const joinRequestForm = ref({
  message: ''
})
const fileList = ref([])

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
    formData.append("fileNames", file.name);
  })

  try {
    console.log(props.recruitID)
    const response = await axios.post(`/plaza/${props.recruitID}`, formData, {
      headers: {
        Authorization: userStore.token,
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.data.code === 1) {
      ElMessage.success('申请提交成功')
      exit();
    } else {
      ElMessage.error('申请提交失败')
    }
  } catch (error) {
    ElMessage.error('网络请求错误')
    console.error(error)
  }
}

// 取消申请
const exit = () => {
  emit('update:visible', false)
}
</script>

<style scoped>
.join-request-container {
  max-width: 900px;
  margin: 0 auto;
}

.request-form-wrapper {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.form-header {
  padding: 20px 24px;
  border-bottom: 1px solid #ebeef5;
}

.form-header h2 {
  margin: 0;
  font-size: 18px;
  color: #333;
  font-weight: 500;
}

.request-form {
  padding: 32px 24px;
  max-width: 800px;
  margin: 0 auto;
}

.request-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

.request-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #333;
  font-size: 15px;
  line-height: 1.5;
  padding-right: 20px;
}

.request-form :deep(.el-input__wrapper),
.request-form :deep(.el-textarea__inner) {
  box-shadow: none;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  transition: all 0.3s;
}

.request-form :deep(.el-input__wrapper:hover),
.request-form :deep(.el-textarea__inner:hover) {
  border-color: #409eff;
}

.custom-textarea {
  width: 100%;
  max-width: 600px;
}

.upload-section {
  width: 100%;
  max-width: 600px;
}

.upload-section :deep(.el-upload__tip) {
  color: #909399;
  font-size: 13px;
  margin-top: 8px;
}

.dialog-footer {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
  text-align: right;
}

.submit-btn,
.cancel-btn {
  padding: 12px 24px;
  font-size: 14px;
  border-radius: 4px;
  margin-left: 12px;
}

.submit-btn {
  background-color: #409eff;
  border-color: #409eff;
}

.submit-btn:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

.cancel-btn {
  background-color: #f56c6c;
  border-color: #f56c6c;
}

.cancel-btn:hover {
  background-color: #f78989;
  border-color: #f78989;
}

:deep(.el-form-item__content) {
  justify-content: flex-start;
  margin-left: 0 !important;
}
</style>