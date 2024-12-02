import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/user': 'http://localhost:8080', // 将 /user 路径的请求代理到后端
    },
  },
})
