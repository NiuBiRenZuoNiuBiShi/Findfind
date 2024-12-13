<script>
import '../assets/jquery-1.12.4.min.js';
import '../assets/bootstrap.min.js';
import {computed} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {useUserStore} from "../stores/userStore.ts";
import {ElMessage} from "element-plus";

export default {
  name: 'Navbar',
  setup() {
    const router = useRouter();
    const route = useRoute(); // 获取当前路由对象
    const userStore = useUserStore();
    const showNavbar = computed(() => route.path !== '/login' && route.path !== '/register');
    const token = localStorage.getItem('token');
    const login = computed(() => token !== null);
    const noLogin = computed(() => token === null);
    // 定义退出登录的方法
    const handleLogout = () => {
      userStore.logout();
      ElMessage.success("成功退出登录");
      setTimeout(() => {
        router.push('/');
        window.location.reload(); // 强制刷新页面
      }, 1000);
    };
    return {
      showNavbar,
      login,
      noLogin,
      handleLogout,
    };
  },
}
</script>

<template>
  <nav v-if="showNavbar" class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container-fluid">
      <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
          </button>
        <a class="navbar-brand" >Find</a>
      </div>
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav">
              <li><router-link to="/">首页广场</router-link></li>
              <li><router-link to="/teamList">队伍列表</router-link></li>
              <li><router-link to="/userList">用户列表</router-link></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
              <li v-if="login"><router-link to="/">我的邀请</router-link></li>
              <li v-if="login"><router-link to="/">我的申请</router-link></li>
              <li v-if="noLogin"><router-link to="/login">登录</router-link></li>
              <li v-if="login" class="dropdown">
                  <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                     aria-expanded="false">个人中心<span class="caret"></span></a>
                  <ul class="dropdown-menu">
                      <li><router-link to="/userInfo">详细信息</router-link></li>
                      <li><router-link to="/">我的队伍</router-link></li>
                      <li role="separator" class="divider">
                      </li>
                      <li><button @click="handleLogout">退出登录</button></li>
                  </ul>
              </li>
          </ul>
          <form class="navbar-form navbar-left">
              <div class="form-group">
                  <input type="text" class="form-control" placeholder="Search">
              </div>
              <button type="submit" class="btn btn-default">Submit</button>
          </form>
      </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
  </nav><!--导航栏-->
</template>

<style scoped>
@import '../assets/bootstrap.min.css';
.container-fluid {
  height: 70px;
  font-size: 20px;
}
</style>