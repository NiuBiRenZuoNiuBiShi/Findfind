<template>
  <div class = "loginContainer">
    <div class="loginBox">
      <h2 class="loginTitle"> 登录 </h2>
      <el-form
          ref="dataFormRef"
          style="max-width: 600px"
          :model="dataForm"
          :rules="rules"
          label-width="auto"
          class="demo-ruleForm"
          :size="formSize"
      >
        <el-form-item label="UserName" prop="username">
          <el-input v-model="dataForm.username" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="Password" prop="password" >
          <el-input v-model="dataForm.password" type="password" autocomplete="new-password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label=" ">
          <el-button @click="handleLogin" type="primary" style="width: 100% "> 登录 </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import axios from "axios";
import router from "../router";
import {useUserStore} from "../stores/userStore";
import {ElMessage} from "element-plus";

const userStore = new useUserStore();

const dataFormRef = ref(null)
const dataForm = ref(
    {
      username: "",
      password: "",
    }
)

const rules = {
  username: [
    {required:true, message: "请输入用户名", trigger: "blur"},
    {required:true, message: "请输入用户名", trigger: "change"}
  ],
  password: [
    {required:true, message: "请输入密码", trigger: "blur"},
    {required:true, message: "请输入密码", trigger: "change"}
  ]
}

const handleLogin = async () => {
  dataFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const response = await axios.post('/user/login', dataForm.value)
        const result = response.data

        if (result.code === 1) {
          const token = result.data
          localStorage.setItem('token', token)
          userStore.token = token
          userStore.userInfo = result.data
          router.push('/')
          ElMessage.success("成功登录")
        } else {
          ElMessage.error("登录失败")
        }
      } catch (error) {
        ElMessage.error(error)
      }
    }
  })

}

</script>


<style>
.loginContainer {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-image: linear-gradient(to top, #fbc2eb 0%, #a6c1ee 100%);
  .loginBox {
    .loginTitle {
      text-align: center;
      margin-bottom: 20px;
    }
    border-radius: 4px;
    height: 200px;
    width: 400px;
    padding: 16px 36px 36px 30px;
    border: 1px solid #dddddd ;
    backdrop-filter: blur(10px) brightness(90%);
    background-color: rgba(255, 255, 255, 0.5);
  }
}
</style>