import {createRouter} from 'vue-router'
import {createWebHistory} from "vue-router";

const routes:any = [
    {
        path: '/login',
        name: 'login',
        component: '@views/Login',
    }
]

const router:any = createRouter({
    routes,
    history: createWebHistory(),
})

export default router