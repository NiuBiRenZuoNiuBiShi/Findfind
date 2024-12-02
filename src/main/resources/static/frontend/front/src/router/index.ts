import {createRouter} from 'vue-router'
import {createWebHistory} from "vue-router";
import Login from '../views/Login.vue';
import {useUserStore} from "../stores/userStore.ts";
const routes:any = [
    {
        path: '/login',
        name: 'login',
        component: Login,
    }
]

const router:any = createRouter({
    routes,
    history: createWebHistory(),
})
router.beforeEach((to, from, next) => {
    const userStore = useUserStore();
    if (userStore.token) {
        if (to.path === '/login') {
            next('/'); // 已登录用户尝试访问登录页面时重定向到主页
        } else {
            next(); // 已登录用户允许访问其他页面
        }
    } else {
        if (to.path === '/login'||to.path === '/') {
            next(); // 未登录用户允许访问登录页面
        } else {
            next('/login'); // 未登录用户尝试访问其他页面时重定向到登录页面
        }
    }
});
export default router