<template>
  <div class="user-profile">
    <h1>更新队伍信息</h1>
    <div class="user-info">
      <el-form
          ref="dataFormRef"
          style="max-width: 600px"
          :model="dataForm"
          :rules="rules"
          label-width="auto"
          class="demo-ruleForm"
          :size="formSize"
      >
        <el-form-item label="Name" prop="name">
          <el-input v-model="dataForm.name" placeholder="请输入队伍名称"/>
        </el-form-item>
        <el-form-item label="Type" prop="type">
          <el-input v-model="dataForm.type" placeholder="请输入队伍类型"/>
        </el-form-item>
        <el-form-item label="Description" prop="description">
          <el-input v-model="dataForm.description" placeholder="请输入队伍描述"/>
        </el-form-item>
        <el-form-item label="Position" prop="position">
          <el-input v-model="dataForm.position" placeholder="请输入队伍位置"/>
        </el-form-item>
        <el-form-item>
          <el-button @click="handleUpdate" type="primary"
                     style="width: 100%; background-image: linear-gradient(to top, #a18cd1 0%, #fbc2eb 100%); margin-top: 10px">
            更新
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script setup>
import {ref, onMounted} from 'vue'
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import {getTeamById} from "../api/getTeamList.ts";
import {updateTeam} from "../api/team.ts";

const router = useRouter();
const getTeamInformation = async () => {
  const id = localStorage.getItem('teamInfo')
  const res = await getTeamById(id);
  console.log('Raw response:', res); // 打印出完整的响应对象
  if (res) {
    return res.data.data;
  } else {
    console.error("Error fetching team information: ", error);
    ElMessage.error("无法获取队伍信息");
    return null;
  }
}

const dataFormRef = ref(null)
const dataForm = ref({
  description: "",
  label: [""],
  name: "",
  type: "",
  position: "",
  status: 1,
  createTime: "",
  updateTime: ""
})

onMounted(async () => {
  const id = localStorage.getItem('teamInfo')
  const teamInfo = await getTeamById(id);
  if (teamInfo) {
    // 假设后端返回的用户信息结构与dataForm的结构一致
    console.log(teamInfo.data.data)
    Object.assign(dataForm.value, teamInfo.data.data);
  }
})
const formSize = "Default"
const rules = {
  name: [
    {required: true, message: "请输入队伍", trigger: "blur"},
    {pattern: /^.{1,20}$/, message: "昵称需要1-20个字符", trigger: "blur"}
  ],
  type: [
    {required: true, message: "请输入队伍类型", trigger: "blur"},
    {pattern: /^.{1,20}$/, message: "队伍类型错误", trigger: "blur"}
  ],
  description: [
    {required: true, message: "请输入队伍描述", trigger: "blur"}
  ],
  position: [
    {required: true, message: "请输入地区", trigger: "blur"}
  ]
}

const handleUpdate = async () => {
  dataFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const team = {
          id: localStorage.getItem('teamInfo'),
          name: dataForm.value.name,
          type: dataForm.value.type,
          description: dataForm.value.description,
          position: dataForm.value.position,
          status: dataForm.value.status,
          createTime: dataForm.value.createTime,
          updateTime: dataForm.value.updateTime,
        }
        console.log(team)
        const res = await updateTeam(team);
        console.log('Raw response:', res); // 打印出完整的响应对象
        if (res.data.code === 1) {
          ElMessage.success("更新成功")
          setTimeout(() => {
            router.push('/teamInfo')
          }, 1000);
        } else if (res.data.message === "队伍名称已存在") {
          ElMessage.error(res.data.message);
        } else {
          console.log(res)
          ElMessage.error("更新失败");
        }
      } catch (error) {
        console.log(error);
      }
    }
  })
}
</script>
<style scoped>
.user-profile {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f7f7f7;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease-in-out; /* 添加过渡效果 */
  padding-top: 60px; /* 假设导航栏高度为60px */
}

.user-profile h1 {
  text-align: center;
  color: #333;
}

.user-info {
  background-color: #fff;
  padding: 15px;
  border-radius: 4px;
  margin-top: 20px;
  transition: box-shadow 0.3s ease-in-out; /* 添加过渡效果 */
}

.user-info ul {
  list-style: none;
  padding: 0;
}

.user-info li {
  margin-bottom: 40px; /* 增加间距 */
  font-size: 20px;
  color: #555;
  transition: background-color 0.3s ease-in-out, color 0.3s ease-in-out; /* 添加过渡效果 */
  padding: 5px 10px; /* 为悬停效果添加一些内边距 */
  border-radius: 4px; /* 为悬停效果添加边框圆角 */
  display: flex; /* 使用flex布局 */
  align-items: center; /* 垂直居中 */
  justify-content: space-between; /* 键和值之间的间距 */
}

.user-info li strong {
  font-weight: 500;
  color: #333;
  margin-right: 20px; /* 在标签和值之间增加一些间距 */
}

.user-info li span {
  flex-grow: 1; /* 让值占据剩余空间 */
}

.user-info li:hover {
  background-color: #e9e9e9; /* 悬停时的背景颜色 */
  color: #333; /* 悬停时的文字颜色 */
}

.no-info {
  text-align: center;
  color: #888;
  margin-top: 20px;
}

/* 添加悬停效果 */
.user-profile:hover, .user-info:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 悬停时的阴影效果 */
}
</style>
