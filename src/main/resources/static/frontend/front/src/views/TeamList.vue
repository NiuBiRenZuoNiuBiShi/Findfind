<script lang="ts" setup>
import {ref} from 'vue'

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
//获取队伍列表
import {getTeamFromUser, getTeamList} from "../api/getTeamList.ts";
import {useUserStore} from "../stores/userStore.ts";
import router from "../router";
import JoinRequestForm from "./JoinRequestForm.vue";
import {ElMessage} from "element-plus";
// 响应式变量
const selectedRecruits = ref([])
const joinFormVisible = ref(false)
const userStore = useUserStore();
const DialogVisible = ref(false);
// 格式化日期时间
const formatDateTime = (dateTime: any) => {
  return dateTime ? new Date(dateTime).toLocaleString() : ''
}
const goToSubmitJoin = async (teamId: number) => {
  const res = await getTeamFromUser()
  for (let i = 0; i < res.data.data.length; i++) {
    if (res.data.data[i].id === teamId) {
      ElMessage.error('不能申请加入自己的队伍')
      return
    }
  }
  DialogVisible.value = true
  selectedRecruits.value = [teamId]
  joinFormVisible.value = true
}
const quitDetails = () => {
  joinFormVisible.value = false
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
        <el-table :data="tableData" stripe style="width: 100%">
          <el-table-column prop="name" label="队伍名称" width="280"/>
          <el-table-column prop="type" label="队伍类型" width="380"/>
          <el-table-column prop="description" label="描述" width="380"/>
          <el-table-column prop="name">
            <template #default="scope">
              <el-button
                  type="success"
                  @click="goToSubmitJoin(scope.row.id)"
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
            width="60%"
            class="custom-dialog"
            destroy-on-close
            v-if="joinFormVisible"
        >
          <JoinRequestForm
              v-if="joinFormVisible"
              v-model:visible="joinFormVisible"
          />
        </el-dialog>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>

</style>