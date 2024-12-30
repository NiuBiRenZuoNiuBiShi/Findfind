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
import {getTeamById} from "../api/getTeamList.ts";
import {deleteTeamById, isTeamLeader, quitTeamById} from "../api/team.ts";
import {ElMessage, ElMessageBox} from "element-plus";
import router from "../router";

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

</script>

<template>
  <div class="user-profile">
    <h1>队伍信息</h1>
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
      <el-button type="danger" @click="quitTeam" v-if="tableData.yourType=='队员' ">退出队伍</el-button>
    </div>
    <div v-else class="no-info">
      <p>暂无队伍信息</p>
    </div>
  </div>
</template>

<style scoped>
.user-profile {
  max-width: 600px;
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
</style>