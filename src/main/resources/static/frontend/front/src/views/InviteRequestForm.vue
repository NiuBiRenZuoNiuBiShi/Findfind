<script setup lang="ts">
import {ref, onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const seekerId = ref(null)
const inviteRequestFormInfo = ref({
  teamId: null,
  message: '',
})
const fileList = ref([])

onMounted(() => {
  seekerId.value = route.params.inviteId
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
  fileList.value.forEach((file) => {
    formData.append("message", file.raw)
  })

  try {
    const res = await axios.post(`/plaza/seeker/invite/${seekerId.value}/${inviteRequestFormInfo.value.teamId}`, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (res.data.code === 1) {
      ElMessage.success('邀请提交成功')
      await router.push('/plaza/seeker')
    } else {
      ElMessage.error('邀请提交失败')
    }
  } catch (err) {
    ElMessage.error(err)
  }
}

</script>

<template>
  <div class="invite-request-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>邀请</span>
        </div>
      </template>

      <el-form ref="inviteRequestFormRef" :model="inviteRequestFormInfo" label-position="left" label-width="150px">
        <el-form-item label="邀请信息" prop="message">
          <el-input
              v-model="inviteRequestFormInfo.message"
              type="textarea"
              placeholder="请输入邀请消息"
              :rows="4"
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
          >
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">
                最多上传5个文件，单个文件不超过10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item label="哪只队伍">
          <el-select></el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitInviteRequest">
            提交邀请
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

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