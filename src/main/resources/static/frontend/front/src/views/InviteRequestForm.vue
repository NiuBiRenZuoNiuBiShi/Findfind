<script setup lang="ts">
import {ref} from 'vue'
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
const userTeams = userStore.userTeams
const fileList = ref([])


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
    });
  }

  try {
    const res = await axios.post(`/plaza/seeker/invite/${props.seekerID}/${inviteRequestFormInfo.value.teamId}`,
        formData, {
          headers: {
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
</script>

<template>
  <div class="invite-request-container">
    <el-card style="margin-top: 150px">
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
          <el-select v-model="inviteRequestFormInfo.teamId" placeholder="选择你的队伍" size="large" clearable>
            <el-option v-for="team in userTeams"
                       :key="team.id"
                       :label="team.name"
                       :value="team.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitInviteRequest">
            提交
          </el-button>
          <el-button @click="quit" type="danger" style="margin-top: -30px">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.invite-request-container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>