<script setup lang="ts">
import {ref} from "vue";

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
import {getTeamFromUser} from "../api/getTeamList.ts";
import router from "../router";
import {ElMessage} from "element-plus";

const getTeamListData = async () => {
  const res = await getTeamFromUser()
  tableData.value = res.data.data
  tableData.value = tableData.value.filter(item => item !== null)
  console.log(tableData.value)
}
getTeamListData()
//获取队伍列表
const Info = (row) => {
  getTeamListData()
  router.push(`/teamInfo`)
  //把点击的行的数据传递到teamInfo页面
  localStorage.setItem('teamInfo', row.id)
  console.log("click")
}

//控制添加队伍弹窗
const dialogVisible = ref(false)

//添加队伍数据模型
const teamModel = ref({
  description: "",
  label: ["Aaa"],
  name: "",
  type: "",
  position: "",
  status: 1
})
//清空数据
const clearData = () => {
  teamModel.value = {
    description: "",
    label: [""],
    name: "",
    type: "",
    position: "",
    status: 1
  }
}
//添加分类表单校验
const rules = {
  teamName: [
    {required: true, message: '请输入队伍名称', trigger: 'blur'},
  ],
  teamType: [
    {required: true, message: '请输入队伍类型', trigger: 'blur'},
  ],
  description: [
    {required: true, message: '请输入队伍描述', trigger: 'blur'},
  ]
}
import {createTeam} from "../api/team.ts";
import {useUserStore} from "../stores/userStore.ts";
//添加分类
const addTeam = async () => {
  if (teamModel.value.name === '' || teamModel.value.type === '' || teamModel.value.description === '') {
    ElMessage({
      message: '请填写完整信息',
      type: 'error'
    })
    return
  }
  //调用接口
  let result = await createTeam(teamModel.value)
  //提示
  ElMessage({
    message: result.data.message,
    type: result.data.code === 1 ? 'success' : 'error'
  })
  if (result.data.code === 1) {
    await useUserStore().initialize()
    window.location.reload()
  }
  //调用获取分类列表接口
  await getTeamListData()
  //关闭弹窗
  dialogVisible.value = false
  clearData()
}
const cancelCreate = () => {
  dialogVisible.value = false
  clearData()
}
const title = ref('添加队伍')
</script>

<template>
  <div></div>
  <div class="common-layout">
    <el-container>
      <el-header>

      </el-header>
      <el-main>
        <el-table :data="tableData" stripe style="width: 100%;font-size: 15px" row-style="height:50px"
                  @row-click="Info">
          <el-table-column prop="name" label="队伍名称" width="180"/>
          <el-table-column prop="type" label="队伍类型" width="180"/>
          <el-table-column prop="description" label="描述"/>
        </el-table>
      </el-main>
    </el-container>
  </div>
  <div class="mb-4 center-button">
    <el-button type="primary" plain @click="dialogVisible=true">创建队伍</el-button>
  </div>
  <!-- 添加队伍弹窗 -->
  <el-dialog v-model="dialogVisible" :title="title" width="30%">
    <el-form :model="teamModel" :rules="rules" label-width="100px" style="padding-right: 30px">
      <el-form-item label="队伍名称" prop="name">
        <el-input v-model="teamModel.name" minlength="1" maxlength="10"></el-input>
      </el-form-item>
      <el-form-item label="队伍类型" prop="type">
        <el-input v-model="teamModel.type" minlength="1" maxlength="15"></el-input>
      </el-form-item>
      <el-form-item label="具体描述" prop="description">
        <el-input v-model="teamModel.description" minlength="1" maxlength="150"></el-input>
      </el-form-item>
      <el-form-item label="位置" prop="position">
        <el-input v-model="teamModel.position" minlength="1" maxlength="15"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
        <span class="dialog-footer">
            <el-button
                @click="cancelCreate">取消</el-button>
            <el-button type="primary" @click="addTeam()"> 确认 </el-button>
        </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.center-button {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>