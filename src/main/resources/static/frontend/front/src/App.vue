<script setup lang="ts">
import Navbar from "./components/Navbar.vue";
import './style.css'
import {onMounted} from "vue";
import {useUserStore} from "./stores/userStore.ts";
const userStore = useUserStore();
onMounted(async() => {
  const token = localStorage.getItem("token");
  if (token) {
    userStore.setToken(token);
    await userStore.initialize();
  }
})
</script>

<template>
  <router-view></router-view>
  <Navbar/>
</template>

<style scoped>

</style>
