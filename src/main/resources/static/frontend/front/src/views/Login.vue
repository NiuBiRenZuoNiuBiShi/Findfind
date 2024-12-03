<template>
  <div class="loginContainer">
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
        <el-form-item label="Password" prop="password">
          <el-input v-model="dataForm.password" type="password" autocomplete="new-password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item >
          <el-button @click="handleLogin" type="primary" style="width: 80% ;
          background-image: linear-gradient(to top, #a18cd1 0%, #fbc2eb 100%); margin-left: 35px;"> 登录
          </el-button>
        </el-form-item>
        <el-form-item >
          <el-button @click="toRegister" type="primary" style="width: 80% ;
          background-image: linear-gradient(to top, #a18cd1 0%, #fbc2eb 100%); margin-left: 35px"> 注册 </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import axios from "axios";
import {useUserStore} from "../stores/userStore";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";

const router = useRouter()
const userStore = new useUserStore();

const dataFormRef = ref(null)
const dataForm = ref(
    {
      username: "",
      password: "",
    }
)
const formSize = "Default"

const rules = {
  username: [
    {required: true, message: "请输入用户名", trigger: "blur"},
    {required: true, message: "请输入用户名", trigger: "change"}
  ],
  password: [
    {required: true, message: "请输入密码", trigger: "blur"},
    {required: true, message: "请输入密码", trigger: "change"}
  ]
}

const handleLogin = async () => {
  console.log(router.currentRoute.value.path)
  dataFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const params = new URLSearchParams()
        params.append('username', dataForm.value.username)
        params.append('password', dataForm.value.password)
        const response = await axios.post('/user/login', params)
        const result = response.data

        if (result.code === 1) {
          const token = result.data
          localStorage.setItem('token', token)
          userStore.token = ref(token)
          userStore.userInfo = ref({
            username: dataForm.value.username,
            password: dataForm.value.password
          })
          await router.push('/')
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

const toRegister = async () => {
  await router.push('/register');
}
</script>


<style scoped>
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
    height: 230px;
    width: 400px;
    padding: 16px 36px 36px 30px;
    border: 1px solid #dddddd;
    backdrop-filter: blur(10px) brightness(90%);
    background-color: rgba(255, 255, 255, 0.5);
  }
}
</style>