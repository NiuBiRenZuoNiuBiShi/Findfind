// src/router.js
import Vue from 'vue';
import VueRouter from 'vue-router';
import routes from './routes'; // 确保 routes 文件存在

Vue.use(VueRouter);

const router = new VueRouter({
    mode: 'history',
    routes,
});

export default router; // 确保这一行存在