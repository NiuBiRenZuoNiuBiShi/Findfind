import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import '@fortawesome/fontawesome-free/css/all.css'
import { createPinia } from "pinia"
// 导入所有图标并进行全局注册
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
const pinia = createPinia()

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app
    .use(ElementPlus, { locale: zhCn })
    .use(router)
    .use(pinia)
    .mount('#app')