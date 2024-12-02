import {createRouter} from 'vue-router'
import {createWebHistory} from "vue-router";
import Login from '../views/Login.vue'

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

export default router