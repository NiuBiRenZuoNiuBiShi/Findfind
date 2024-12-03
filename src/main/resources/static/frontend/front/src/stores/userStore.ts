import {defineStore} from "pinia";
import {ref} from "vue";

export const useUserStore = defineStore("user", () => {
    const token = ref("");
    const userInfo = ref({});
    function logout() {
        token.value = '';
        userInfo.value = {};
        localStorage.removeItem('token');
        window.location.reload(); // 强制刷新页面
    }
    // 定义一个函数来获取当前用户信息
    function getCurrentUser() {
        return {
            token: token.value,
            info: userInfo.value
        };
    }
    return { token, userInfo, logout, getCurrentUser };
})