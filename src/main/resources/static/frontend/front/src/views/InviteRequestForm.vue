<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {ElMessage} from 'element-plus'
import axios from 'axios'
import {useUserStore} from "../stores/userStore.ts";

const userStore = useUserStore()
const inviteRequestFormRef = ref(null)
const props = defineProps({
  seekerID: {
    type: Number,
    required: true
  },
  visible: {
    type: Boolean,
    required: true
  }
})
const emit = defineEmits(["update:visible"])

const inviteRequestFormInfo = ref({
  teamId: null,
  message: '',
})
const userTeams = ref(null)
const fileList = ref([])

onMounted(async () => {
  await userStore.initialize(); // 确保数据已加载
  userTeams.value = userStore.userTeams;
})

const handleExceed = () => {
  ElMessage.warning("最多五个文件")
}

const submitInviteRequest = async () => {
  if (!inviteRequestFormInfo.value.message.trim) {
    ElMessage.error("请填写邀请信息")
    return
  }

  const formData = new FormData();

  formData.append("message", inviteRequestFormInfo.value.message)
  formData.append("teamId", inviteRequestFormInfo.value.teamId)
  if (fileList.value && fileList.value.length > 0) {
    fileList.value.forEach((file) => {
      formData.append("files", file.raw);
      formData.append("fileNames", file.name);
    });
  }

  try {
    const res = await axios.post(`/plaza/seeker/invite/${props.seekerID}/${inviteRequestFormInfo.value.teamId}`,
        formData, {
          headers: {
            Authorization: userStore.token,
            'Content-Type': 'multipart/form-data'
          }
        })

    if (res.data.code === 1) {
      ElMessage.success('邀请提交成功')
      quit()
    } else {
      ElMessage.error('邀请提交失败')
    }
  } catch (err) {
    ElMessage.error(err)
  }
}

const quit = () => {
  emit('update:visible', false)
}

const handleChange = (file, theFileList) => {
  fileList.value = theFileList
}
</script>

<template>
  <div class="invite-request-container">
    <el-form
        ref="inviteRequestFormRef"
        :model="inviteRequestFormInfo"
        label-position="left"
        label-width="120px"
        class="invite-form"
    >
      <el-form-item label="邀请信息" prop="message">
        <el-input
            v-model="inviteRequestFormInfo.message"
            type="textarea"
            placeholder="请输入邀请消息"
            :rows="4"
            class="custom-textarea"
        ></el-input>
      </el-form-item>

      <el-form-item label="上传文件">
        <el-upload
            v-model:file-list="fileList"
            action=""
            :auto-upload="false"
            multiple
            :limit="5"
            :on-exceed="handleExceed"
            :on-change="handleChange"
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

      <el-form-item label="选择队伍">
        <el-select
            v-model="inviteRequestFormInfo.teamId"
            placeholder="选择你的队伍"
            size="large"
            clearable
            class="team-select"
        >
          <el-option
              v-for="team in userTeams"
              :key="team.id"
              :label="team.name"
              :value="team.id"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item class="dialog-footer">
        <el-button type="primary" @click="submitInviteRequest" class="submit-btn">提交</el-button>
        <el-button @click="quit" type="danger" class="cancel-btn">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped>
.invite-request-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  max-width: 900px;
  margin: 0 auto;
}

.invite-form {
  max-width: 800px;
  margin: 0 auto;
  padding: 32px 24px;
}

.invite-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

.invite-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #333;
  font-size: 15px;
  line-height: 1.5;
  padding-right: 20px;
}

.invite-form :deep(.el-input__wrapper),
.invite-form :deep(.el-textarea__inner) {
  box-shadow: none;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  transition: all 0.3s;
}

.invite-form :deep(.el-input__wrapper:hover),
.invite-form :deep(.el-textarea__inner:hover) {
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

.team-select {
  width: 100%;
  max-width: 600px;
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
  margin-bottom: -30px;
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