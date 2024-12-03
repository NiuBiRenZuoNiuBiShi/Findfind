<template>
  <div class="loginContainer">
    <div class="loginBox">
      <h2 class="loginTitle"> 注册 </h2>
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
        <el-form-item label="Nickname" prop="nickname">
          <el-input v-model="dataForm.nickname" placeholder="请输入昵称"/>
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input v-model="dataForm.password" type="password" autocomplete="off" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="PasswordConfirm" prop="passwordConfirm">
          <el-input v-model="dataForm.passwordConfirm" type="password" autocomplete="off" placeholder="请确认密码"/>
        </el-form-item>
        <el-form-item label="Email(Not Necessary)" prop="email">
          <el-input v-model="dataForm.email" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item label="Phone(Not Necessary)" prop="phoneNumber">
          <el-input v-model="dataForm.phoneNumber" placeholder="请输入手机号"/>
        </el-form-item>
        <el-form-item >
          <el-button @click="handleRegister" type="primary" style="width: 80% ;
          background-image: linear-gradient(to top, #a18cd1 0%, #fbc2eb 100%); margin-left: 35px; margin-top: 10px">
            注册
          </el-button>
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
      nickname: "",
      password: "",
      passwordConfirm: "",
      email: "",
      phoneNumber: "",
    }
)
const formSize = "Default"

const rules = {
  username: [
    {required: true, message: "请输入用户名", trigger: "blur"},
    {required: true, message: "请输入用户名", trigger: "change"},
    {
      pattern: /^.{4,20}$/,
      message: "需要4-20任意字符",
      trigger: "blur"
    }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { required: true, message: "请输入密码", trigger: "change" },
    {
      pattern: /^[A-Za-z0-9.]{4,20}$/, // 正则表达式：字母、数字、句点，长度为4到20个字符
      message: "密码必须是4到20个字母、数字或句点（.）",
      trigger: "blur"
    }
  ],
  passwordConfirm: [
    {required: true, message: "与上方保持一致", trigger: "blur"},
    {required: true, message: "与上方保持一致", trigger: "change"},
    {
      required: true,
      validator: (rule, value, callback) => {
        if (value !== dataForm.value.password) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      }, trigger: "blur"
    }
  ],
  nickname: [
    {required: true, message: "需要1-20任意字符", trigger: "blur"},
    {required: true, message: "需要1-20任意字符", trigger: "change"},
    {
      pattern: /^.{1,20}$/,
      message: "需要1-20任意字符",
      trigger: "blur"
    }
  ],
  email: [
    {required:true, message: "请输入邮箱", trigger: "blur"},
    {required:true, message: "请输入邮箱", trigger: "change"},
    {
      pattern: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
      message: "邮箱格式错误",
      trigger: "blur"
    }
  ],
  phoneNumber: [
    {required:true, message: "请输入手机号", trigger: "blur"},
    {required:true, message: "请输入手机号", trigger: "change"},
    {
      pattern: /^[0-9]{11}$/,
      message: "手机号格式错误",
      trigger: "blur"
    }
  ]
}

const handleRegister = async () => {
  dataFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const user = {
          username: dataForm.value.username,
          nickName: dataForm.value.nickname,
          password: dataForm.value.password,
          email: dataForm.value.email,
          phone: dataForm.value.phoneNumber,
          status: 1,
          position: "",
          biology: "暂无"
        }
        console.log(user)
        const response = await axios.post("user/register", user)
        const result = response.data;
        if (result.code === 1) {
          ElMessage.success("2s后进入登录界面")
          setTimeout(() => { router.push('/login') }, 1000);
        } else {
          console.log(result)
          ElMessage.error("注册失败");
        }
      } catch (error) {
        console.log(error);
      }
    }
  })
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
      margin-top: 5px;
    }

    border-radius: 4px;
    height: 380px;
    width: 400px;
    padding: 16px 36px 36px 30px;
    border: 1px solid #dddddd;
    backdrop-filter: blur(10px) brightness(90%);
    background-color: rgba(255, 255, 255, 0.5);
  }
}
</style>