<template>
  <div class = "loginContainer">
    <div class="loginBox">
      <h2 class="loginTitle"> 登录 </h2>
      <el-form
          ref="ruleForm"
          style="max-width: 600px"
          :model="dataFrom"
          :rules="rules"
          label-width="auto"
          class="demo-ruleForm"
          :size="formSize"
      >
        <el-form-item label="UserName" prop="username">
          <el-input v-model="dataFrom.username" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="Password" prop="password" >
          <el-input v-model="dataFrom.password" type="password" autocomplete="new-password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label=" ">
          <el-button type="primary" style="width: 100% "> 登录 </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const dataFrom = ref(
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