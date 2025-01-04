<script setup lang="ts">
import {ref} from "vue";

const tableData = ref({
  "name": "test",
  "type": "test",
  "description": "test",
  "id": 1,
  "label": "test",
  "position": "test",
  "status": "test",
  "createTime": "2021-08-17T07:00:00.000+00:00",
  "updateTime": "2021-08-17T07:00:00.000+00:00",
  yourType: "队员"
});
import {getTeamById, getTeamUsers} from "../api/getTeamList.ts";
import {deleteTeamById, isTeamLeader, isTeamLeaderByUserId, quitTeamById} from "../api/team.ts";
import {ElMessage, ElMessageBox} from "element-plus";
import router from "../router";
import {sendEmailto} from "../api/sendEmail.ts";

const getTeamListData = async () => {
  const id = localStorage.getItem('teamInfo')
  console.log(id)
  const res = await getTeamById(id)
  tableData.value = res.data.data
  console.log(tableData.value)
  const leader = await isTeamLeader(id)
  console.log(leader.data.data)
  if (leader.data.data === true) {
    tableData.value.yourType = "队长"
  } else {
    tableData.value.yourType = "队员"
  }
}
getTeamListData()

//删除队伍
const deleteTeam = () => {
  const id = localStorage.getItem('teamInfo')
  //确认框
  //删除分类  给删除按钮绑定事件
  ElMessageBox.confirm(
      '你确认删除此队伍吗？',
      '温馨提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(() => {
    //用户点击了确认
    ElMessage({
      type: 'success',
      message: '删除成功',
    })
    //调用接口
    deleteTeamById(id)
    //调用获取分类列表接口  刷新
    getTeamListData()
    router.push(`/team`)
  }).catch(() => {
    //用户点击了取消
    ElMessage({
      type: 'info',
      message: '取消删除',
    })
  })
}

//退出队伍
const quitTeam = () => {
  const id = localStorage.getItem('teamInfo')
  //确认框
  //删除分类  给删除按钮绑定事件
  ElMessageBox.confirm(
      '你确认退出此队伍吗？',
      '温馨提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(() => {
    //用户点击了确认
    ElMessage({
      type: 'success',
      message: '退出成功',
    })
    //调用接口
    quitTeamById(id)
    //调用获取分类列表接口  刷新
    getTeamListData()
    router.push(`/team`)
  }).catch(() => {
    //用户点击了取消
    ElMessage({
      type: 'info',
      message: '取消退出',
    })
  })
}

const teamMemmbers = ref([
  {
    "id": 1,
    "username": "test",
    "nickName": "test",
    "type": "test",
    "email": "test",
    "phone": "test"
  },
  {
    "id": 2,
    "username": "test",
    "nickName": "test",
    "type": "test",
    "email": "test",
    "phone": "test"
  }
])
const getMembers = async () => {
  const id = localStorage.getItem('teamInfo')
  const res = await getTeamUsers(id)
  teamMemmbers.value = res.data.data
  for (let i = 0; i < teamMemmbers.value.length; i++) {
    const res2 = await isTeamLeaderByUserId(id, teamMemmbers.value[i].id)
    if (res2.data.data === true) {
      teamMemmbers.value[i].type = "队长"
    } else {
      teamMemmbers.value[i].type = "队员"
    }
  }
  //把队长放在第一个
  for (let i = 0; i < teamMemmbers.value.length; i++) {
    if (teamMemmbers.value[i].type === "队长") {
      const temp = teamMemmbers.value[0]
      teamMemmbers.value[0] = teamMemmbers.value[i]
      teamMemmbers.value[i] = temp
    }
  }
}
getMembers()
const handleChange = (val: string[]) => {
  console.log(val)
}

const dialogVisible = ref(false)
const email = ref(
    {
      tos: "",
      subject: "",
      content: ""
    }
)
const sendEmailDia = (to: string) => {
  dialogVisible.value = true
  email.value.tos = to
}
const closeDia = () => {
  dialogVisible.value = false
  email.value.subject = ""
  email.value.content = ""
}
const sendEmail = () => {
  ElMessageBox.confirm(
      '你确认发送邮件吗？',
      '温馨提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(() => {
    if (email.value.subject === "" || email.value.content === "") {
      ElMessage({
        type: 'error',
        message: '请填写完整信息',
      })
      return
    }
    //用户点击了确认
    ElMessage({
      type: 'success',
      message: '发送成功',
    })
    //调用接口
    sendEmailto(email.value)
    closeDia()
  }).catch(() => {
    //用户点击了取消
    ElMessage({
      type: 'info',
      message: '取消发送',
    })
  })
}
</script>

<template>
  <el-dialog
      title="发送邮件"
      v-model="dialogVisible"
      width="40%"
      :before-close="closeDia"
  >
    <el-form>
      <el-form-item label="收件人">
        <el-input v-model="email.tos" disabled></el-input>
      </el-form-item>
      <el-form-item label="主题">
        <el-input v-model="email.subject"></el-input>
      </el-form-item>
      <el-form-item label="内容">
        <el-input type="textarea" :autosize="{minRows:8}" v-model="email.content"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDia">取 消</el-button>
      <el-button type="primary" @click="sendEmail">确 定</el-button>
    </span>
  </el-dialog>
  <div class="user-profile">
    <h1 style="flex-direction: column">队伍信息</h1>
    <div style="display: flex">
      <div class="demo-collapse float" style="width: 200px">
        <div class="users-info">
          队伍成员
        </div>
        <el-collapse>
          <el-collapse-item v-for="(item) in teamMemmbers" @change="handleChange" class="users-info">
            <template #title>
              <div class="custom-title">
                {{ item.username }}
              </div>
            </template>
            <div>
              <ul>
                <li>
                  <strong>昵称:</strong> <span>{{ item.nickName }}</span>
                </li>
                <li>
                  <strong>身份:</strong> <span>{{ item.type }}</span>
                </li>
                <li>
                  <strong>电话:</strong> <span>{{ item.phone }}</span>
                </li>
                <li>
                  <strong>邮箱:</strong> <span>{{ item.email }}</span>
                </li>
                <li v-if="item.type=='队员' ">
                  <el-button type="primary" @click="sendEmailDia(item.email)">发送邮件</el-button>
                </li>
                <li v-if="item.type=='队员' ">
                  <el-button type="danger">踢出队伍</el-button>
                </li>
              </ul>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div v-if="tableData" class="user-info">
        <ul>
          <li>
            <strong>队伍名:</strong> <span>{{ tableData.name }}</span>
          </li>
          <li>
            <strong>队伍类型:</strong> <span>{{ tableData.type }}</span>
          </li>
          <li>
            <strong>队伍描述:</strong> <span>{{ tableData.description }}</span>
          </li>
          <li>
            <strong>队伍状态:</strong> <span>{{ tableData.status }}</span>
          </li>
          <li>
            <strong>队伍位置:</strong> <span>{{ tableData.position }}</span>
          </li>
          <li>
            <strong>您的身份:</strong> <span>{{ tableData.yourType }}</span>
          </li>
        </ul>
        <router-link to="/TeamUpdate" class="update-button" v-if="tableData.yourType=='队长' ">
          修改队伍信息
        </router-link>
        <el-button type="danger" @click="deleteTeam" v-if="tableData.yourType=='队长' ">删除队伍</el-button>
        <el-button type="danger" @click="quitTeam">退出队伍</el-button>
        <router-link to="/handleJoin" class="join-button" v-if="tableData.yourType=='队长' ">
          查看他人申请
        </router-link>
      </div>
      <div v-else class="no-info">
        <p>暂无队伍信息</p>
      </div>
    </div>
  </div>

</template>

<style scoped>
.custom-title {
  color: #333;
  font-size: 20px;
}

.float {
  float: left;
}

.user-profile {
  max-width: 1000px;
  margin: 0 auto;
  padding: 25px;
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

.users-info {
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

.update-button {
  display: block;
  width: max-content;
  padding: 10px 20px;
  margin: 20px auto;
  background-color: #42b983;
  color: white;
  text-align: center;
  border-radius: 5px;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

.update-button:hover {
  background-color: #388e7c;
}

.join-button {
  width: max-content;
  padding: 10px 20px;
  margin-left: 235px;
  background-color: #42b983;
  color: white;
  text-align: center;
  border-radius: 5px;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

.join-button:hover {
  background-color: #388e7c;
}


.users-info ul {
  list-style: none;
  padding: 0;
}

.users-info li {
  margin-bottom: 10px; /* 增加间距 */
  font-size: 15px;
  color: #555;
  transition: background-color 0.3s ease-in-out, color 0.3s ease-in-out; /* 添加过渡效果 */
  padding: 5px 10px; /* 为悬停效果添加一些内边距 */
  border-radius: 4px; /* 为悬停效果添加边框圆角 */
  align-items: center; /* 垂直居中 */
  justify-content: space-between; /* 键和值之间的间距 */
}

.users-info li strong {
  font-weight: 500;
  color: #333;
  margin-right: 10px; /* 在标签和值之间增加一些间距 */
}

.users-info li span {
  flex-grow: 1; /* 让值占据剩余空间 */
}

.users-info li:hover {
  background-color: #e9e9e9; /* 悬停时的背景颜色 */
  color: #333; /* 悬停时的文字颜色 */
}

</style>