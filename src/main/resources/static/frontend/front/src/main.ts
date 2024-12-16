import {createApp} from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import {createPinia} from "pinia";
const pinia = createPinia();

createApp(App)
    .use(ElementPlus, {locale: zhCn})
    .use(router)
    .use(pinia)
    .mount('#app')
