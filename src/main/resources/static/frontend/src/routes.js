// src/routes.js
const routes = [
    {
        path: '/',
        name: 'Hello',
        component: () => import('./components/HelloWorld.vue')
    }
    // ...其他路由
];
export default routes;
    // 其他路由...
