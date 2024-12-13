<script lang="ts" setup>
import {ref,onMounted} from 'vue';
import {getUserInfo } from '../api/getUserInfo';
interface User {
    id: number;
    username: string;
    nickName: string;
    email: string;
    phone: string;
    biology: string;
    position: string;
    status: string;
    createTime: string;
    updateTime: string;
}
const tableData = ref(null);
onMounted(async () => {
    try {

        const res = await getUserInfo();
        console.log('Raw response:', res); // 打印出完整的响应对象
        if (res){
            tableData.value = res.data.data as User; // 将用户列表赋值给tableData
        }
        else{
            console.error('API response is not a User ', res.data.data);
        }
    } catch (error) {
        console.error('Failed to fetch user list:', error);
    }
});
</script>

<template>
    <div class="user-profile">
        <h1>个人信息</h1>
        <div v-if="tableData" class="user-info">
            <ul>
                <li>
                    <strong>用户名:</strong> <span>{{ tableData.username }}</span>
                </li>
                <li>
                    <strong>昵称:</strong> <span>{{ tableData.nickName }}</span>
                </li>
                <li>
                    <strong>电话:</strong> <span>{{ tableData.phone }}</span>
                </li>
                <li>
                    <strong>地区:</strong> <span>{{ tableData.position }}</span>
                </li>
                <li>
                    <strong>邮箱:</strong> <span>{{ tableData.email }}</span>
                </li>
                <li>
                    <strong>个人简介:</strong> <span>{{ tableData.biology }}</span>
                </li>
            </ul>
        </div>
        <div v-else class="no-info">
            <p>暂无个人信息</p>
        </div>
    </div>
</template>
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
