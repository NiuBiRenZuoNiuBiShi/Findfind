<template>
    <div class="user-profile">
        <h1>更新用户信息</h1>
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
                <el-form-item label="Nickname" prop="nickName">
                    <el-input v-model="dataForm.nickName" placeholder="请输入昵称"/>
                </el-form-item>
                <el-form-item label="Email" prop="email">
                    <el-input v-model="dataForm.email" placeholder="请输入邮箱"/>
                </el-form-item>
                <el-form-item label="Phone" prop="phone">
                    <el-input v-model="dataForm.phone" placeholder="请输入手机号"/>
                </el-form-item>
                <el-form-item label="Biology" prop="biology">
                    <el-input v-model="dataForm.biology" placeholder="请输入个人简介"/>
                </el-form-item>
                <el-form-item label="Position" prop="position">
                    <el-input v-model="dataForm.position" placeholder="请输入地区"/>
                </el-form-item>
                <el-form-item label="Status" prop="status">
                    <el-input v-model="dataForm.status" placeholder="请输入状态"/>
                </el-form-item>
                <el-form-item >
                    <el-button @click="handleUpdate" type="primary" style="width: 100%; background-image: linear-gradient(to top, #a18cd1 0%, #fbc2eb 100%); margin-top: 10px">
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
import {getUserInfo } from '../api/getUserInfo';
import  {userUpdate} from '../api/userUpdate';
const router = useRouter();
const getUserInformation = async () => {
    const res = await getUserInfo();
    console.log('Raw response:', res); // 打印出完整的响应对象
    if (res){
        return res.data.data;
    }
    else{
        console.error("Error fetching user information: ", error);
        ElMessage.error("无法获取用户信息");
        return null;
    }
}

const dataFormRef = ref(null)
const dataForm = ref({
    username:"",
    id:0,
    nickName: "",
    email: "",
    phone: "",
    biology: "",
    position: "",
    status: "",
    createTime: "",
    updateTime: ""
})

onMounted(async () => {
    const userInfo = await getUserInformation();
    if (userInfo) {
        // 假设后端返回的用户信息结构与dataForm的结构一致
        Object.assign(dataForm.value, userInfo);
    }
})
const formSize = "Default"
const rules = {
    nickName: [
        {required: true, message: "请输入昵称", trigger: "blur"},
        {pattern: /^.{1,20}$/, message: "昵称需要1-20个字符", trigger: "blur"}
    ],
    email: [
        {required: true, message: "请输入邮箱", trigger: "blur"},
        {pattern: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/, message: "邮箱格式错误", trigger: "blur"}
    ],
    phone: [
        {required: true, message: "请输入手机号", trigger: "blur"},
        {pattern: /^[0-9]{11}$/, message: "手机号格式错误", trigger: "blur"}
    ],
    biology: [
        {required: true, message: "请输入个人简介", trigger: "blur"}
    ],
    position: [
        {required: true, message: "请输入地区", trigger: "blur"}
    ],
    status: [
        {required: true, message: "请输入状态", trigger: "blur"}
    ]
}

const handleUpdate = async () => {
    dataFormRef.value.validate(async (valid) => {
        if (valid) {
            try {
                const user = {
                    username:dataForm.value.username,
                    id: dataForm.value.id,
                    nickname: dataForm.value.nickName,
                    email: dataForm.value.email,
                    phone: dataForm.value.phone,
                    biology: dataForm.value.biology,
                    position: dataForm.value.position,
                    status: dataForm.value.status,
                    createTime: dataForm.value.createTime,
                    updateTime: dataForm.value.updateTime,
                }
                console.log(user)
                const res = await userUpdate(user);
                console.log('Raw response:', res); // 打印出完整的响应对象
                if (res.data.code === 1) {
                    ElMessage.success("更新成功")
                    setTimeout(() => { router.push('/userInfo') }, 1000);
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
