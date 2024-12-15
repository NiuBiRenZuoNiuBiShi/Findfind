import {createRouter} from 'vue-router'
import {createWebHistory} from "vue-router";
import Login from '../views/Login.vue';
import Register from "../views/Register.vue";
import App from "../App.vue";
import TeamList from '../views/TeamList.vue';
import Plaza from "../views/RecruitPlaza.vue";
import JoinRequestForm from "../views/JoinRequestForm.vue";
import SeekerPlaza from "../views/SeekerPlaza.vue";
import InviteRequestForm from "../views/InviteRequestForm.vue";
import CreateRecruitForm from "../views/CreateRecruitForm.vue";
import UserList from "../views/UserList.vue";
import UserInfo from "../views/UserInfo.vue";
import Team from "../views/Team.vue";

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
        path: '/team',
        name: 'team',
        component: Team,
    },
    {
        path: '/teamList',
        name: 'teamList',
        component: TeamList,
    },
    {
        path: '/userList',
        name: 'userList',
        component: UserList
    },
    {
        path: '/userInfo',
        name: 'userInfo',
        component: UserInfo
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
    },
    {
        path: '/plaza/seeker',
        name: 'seeker',
        component: SeekerPlaza,
    },
    {
        path: '/plaza/seeker/:seekerId',
        name: 'InviteRequest',
        component: InviteRequestForm,
        meta: {
            title: '邀请用户'
        }
    },
    {
        path: '/createRecruit',
        name: 'createRecruit',
        component: CreateRecruitForm
    },

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