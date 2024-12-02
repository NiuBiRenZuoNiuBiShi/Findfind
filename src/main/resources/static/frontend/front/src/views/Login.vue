<script setup lang="ts">
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";

const router = useRouter();

const form = reactive({
  username: "",
  password: "",
});

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};

const loginForm = ref();

const handleLogin = async () => {
  await loginForm.value?.validate();
  const response = await fetch("/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(form),
  });
  const result = await response.json();
  if (result.code === 1) {
    ElMessage.success("登录成功");
    await router.push("/plaza");
  } else {
    ElMessage.error("登录失败，请检查用户名和密码");
  }
};

</script>

<template>
  <div class="login-container">
    <el-form
        :model="form"
        :rules="rules"
        ref="loginForm"
        label-width="80px"
        class="login-form"
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleLogin">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-form {
  width: 400px;
  padding: 20px;
  background: white;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}
</style>