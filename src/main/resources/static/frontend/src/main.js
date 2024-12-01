import { createApp } from 'vue';
import App from './App.vue';
//import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElIcons from '@element-plus/icons-vue'

// 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElIcons)) {
    app.component(key, component)
}

const app = createApp(App);
app.mount('#app');