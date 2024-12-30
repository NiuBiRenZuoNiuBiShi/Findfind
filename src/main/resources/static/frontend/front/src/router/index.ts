import {createRouter} from 'vue-router'
import {createWebHistory} from "vue-router";
import Login from '../views/Login.vue';
import Register from "../views/Register.vue";
import TeamList from '../views/TeamList.vue';
import JoinRequestForm from "../views/JoinRequestForm.vue";
import SeekerPlaza from "../views/SeekerPlaza.vue";
import InviteRequestForm from "../views/InviteRequestForm.vue";
import CreateRecruitForm from "../views/CreateRecruitForm.vue";
import UserList from "../views/UserList.vue";
import UserInfo from "../views/UserInfo.vue";
import UserUpdate from "../views/UserUpdate.vue";
import TeamInfo from '../views/TeamInfo.vue';
import UserInvites from "../views/UserInvites.vue";
import UserJoins from "../views/UserJoins.vue";
import CreateSeekerForm from "../views/CreateSeekerForm.vue";
import RecruitPlaza from "../views/RecruitPlaza.vue";
import TeamUpdate from "../views/TeamUpdate.vue";
import UserSeekers from "../views/UserSeekers.vue";
import App from "../App.vue";

const Team = () => import('../views/Team.vue');
const routes: any = [
    {
        path: '/login',
        name: 'login',
        component: Login
    },
    {
        path: '/register',
        name: 'register',
        component: Register
    },

    {
        path: '/team',
        name: 'team',
        component: Team
    },
    {
        path: '/teamList',
        name: 'teamList',
        component: TeamList,
    },
    {
        path: '/teamInfo',
        name: 'teamInfo',
        component: TeamInfo
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
        path: '/update',
        name: 'userUpdate',
        component: UserUpdate
    },
    {
        path: '/userInvites',
        name: 'userInvites',
        component: UserInvites
    },
    {
        path: '/userJoins',
        name: 'userJoins',
        component: UserJoins
    },
    {
        path: '/userSeekers',
        name: 'userSeekers',
        component: UserSeekers
    },
    {
        path: '/recruitPlaza',
        name: 'recruitPlaza',
        component: RecruitPlaza,
    },
    {
        path: '/recruitPlaza/:recruitId',
        name: 'JoinRequest',
        component: JoinRequestForm,
        meta: {
            title: '申请加入'
        }
    },
    {
        path: '/seekerPlaza',
        name: 'seekerPlaza',
        component: SeekerPlaza,
    },
    {
        path: '/seekerPlaza/:seekerId',
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
    {
        path: '/createSeeker',
        name: 'createSeeker',
        component: CreateSeekerForm
    },
    {
        path: '/teamUpdate',
        name: 'teamUpdate',
        component: TeamUpdate
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