import {createRouter} from 'vue-router'
import {createWebHistory} from "vue-router";
import Login from '../views/Login.vue';
import Register from "../views/Register.vue";
import App from "../App.vue";
import TeamList from '../views/TeamList.vue';
import Plaza from "../views/Plaza.vue";
import JoinRequestForm from "../views/JoinRequestForm.vue";

const routes: any = [
    {
        path: '/login',
        name: 'login',
        component: Login,
    },
    {
        path: '/register',
        name: 'register',
        component: Register,
    },
    {
        path: '/',
        name: 'index',
        component: App,
    },
    {
        path: '/teamList',
        name: 'teamList',
        component: TeamList,
    },
    {
        path: '/plaza',
        name: 'plaza',
        component: Plaza,
    },
    {
        path: '/plaza/:recruitId',
        name: 'JoinRequest',
        component: JoinRequestForm,
        meta: {
            title: '申请加入'
        }
    }

]

const router: any = createRouter({
    routes,
    history: createWebHistory(),
})

router.beforeEach((to, from, next): void => {
    const token = localStorage.getItem('token');
    if (token) {
        if (to.path === '/login') {
            next('/'); // 已登录用户尝试访问登录页面时重定向到主页
        } else {
            next(); // 已登录用户允许访问其他页面
        }
    } else {
        if (to.path === '/login' || to.path === '/' || to.path === '/register') {
            next(); // 未登录用户允许访问登录页面
        } else {
            next('/login'); // 未登录用户尝试访问其他页面时重定向到登录页面
        }
    }
});
export default router