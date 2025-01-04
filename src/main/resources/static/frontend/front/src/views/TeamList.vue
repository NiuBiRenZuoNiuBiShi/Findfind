<script lang="ts" setup>
import {ref} from 'vue'
//获取队伍列表
import {getTeamFromUser, getTeamList} from "../api/getTeamList.ts";
import {useUserStore} from "../stores/userStore.ts";
import {ElMessage} from "element-plus";
import {instance} from "../api/user.ts";
import {createJoinRequest} from "../api/joinRequest.ts";

const tableData = ref([
  {
    "name": "test",
    "type": "test",
    "description": "test",
    "id": 1,
    "label": "test",
    "position": "test",
    "status": "test",
    "createTime": "2021-08-17T07:00:00.000+00:00",
    "updateTime": "2021-08-17T07:00:00.000+00:00"
  },
])
// 响应式变量
const selectedRecruits = ref([])
const joinFormVisible = ref(false)
const userStore = useUserStore();
const DialogVisible = ref(false);
const joinRequestForm = ref({
  name: '',
  teamId: '',
  userId: '',
  message: ''
})
const rules = {
  message: [
    {required: true, message: '请输入申请理由', trigger: 'blur'}
  ],
}
// 格式化日期时间
const formatDateTime = (dateTime: any) => {
  return dateTime ? new Date(dateTime).toLocaleString() : ''
}

const goToSubmitJoin = async (teamId: string, name: string) => {
  const res = await getTeamFromUser()
  for (let i = 0; i < res.data.data.length; i++) {
    if (res.data.data[i].id === teamId) {
      ElMessage.error('不能申请加入已在的队伍')
      return
    }
  }
  DialogVisible.value = true
  selectedRecruits.value = [teamId]
  joinFormVisible.value = true
  joinRequestForm.value.teamId = teamId
  const token = localStorage.getItem('token');
  const userAll = await instance.get('/user/userInfo', {
    headers: {
      Authorization: token
    }
  });
  joinRequestForm.value.userId = userAll.data.data.id
  joinRequestForm.value.name = name
}

const quitDetails = () => {
  joinFormVisible.value = false
  DialogVisible.value = false
  joinRequestForm.value = {
    name: '',
    teamId: '',
    userId: '',
    message: ''
  }
}

const joinTeam = async () => {
  if (!joinRequestForm.value.message.trim()) {
    ElMessage.error('请填写申请理由')
    return
  }
  const res = await createJoinRequest(joinRequestForm.value.teamId
      , joinRequestForm.value.userId, joinRequestForm.value.message)
  console.log(res)
  if (res.data.code === 1) {
    ElMessage.success('申请提交成功')
    quitDetails()
  } else if (res.data.message === '已经发送过请求') {
    ElMessage.error('已经发送过申请')
  } else {
    ElMessage.error('申请提交失败')
  }
}

const getTeamListData = async () => {
  const res = await getTeamList()
  tableData.value = res.data.data
  console.log(tableData.value)
}
getTeamListData()
//获取队伍列表
</script>
<template>
  <div class="common-layout">
    <el-container>
      <el-header>
      </el-header>
      <el-main>
        <el-table :data="tableData" stripe style="width: 100%;font-size: 15px" row-style="height:50px">
          <el-table-column prop="name" label="队伍名称" width="280"/>
          <el-table-column prop="type" label="队伍类型" width="380"/>
          <el-table-column prop="description" label="描述" width="380"/>
          <el-table-column prop="name">
            <template #default="scope">
              <el-button
                  type="success"
                  @click="goToSubmitJoin(scope.row.id, scope.row.name)"
                  class="join-button"
              >
                申请加入
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog
            v-model="DialogVisible"
            title="邀请"
            width="50%"
            class="custom-dialog"
            destroy-on-close
            v-if="joinFormVisible"
            :before-close="quitDetails"
        >
          <el-form :model="joinRequestForm" :rules="rules" label-width="100px" style="padding-right: 30px">
            <el-form-item label="加入队伍" prop="name" size="large" style="margin-bottom: 40px">
              <el-input v-model="joinRequestForm.name" minlength="1" maxlength="1500"
                        type="text"
                        size="large"
                        disabled="disabled"
                        style="font-size: 20px;"
                        :autosize="{ minRows: 1, maxRows: 1 }"></el-input>
            </el-form-item>
            <el-form-item label="加入理由" prop="message">
              <el-input v-model="joinRequestForm.message" minlength="1" maxlength="1500"
                        type="textarea"
                        :autosize="{ minRows: 15, maxRows: 100 }"></el-input>
            </el-form-item>
          </el-form>
          <template #footer>
        <span class="dialog-footer">
            <el-button
                @click="quitDetails">取消</el-button>
            <el-button type="primary" @click="joinTeam()"> 确认 </el-button>
        </span>
          </template>
        </el-dialog>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>

</style>