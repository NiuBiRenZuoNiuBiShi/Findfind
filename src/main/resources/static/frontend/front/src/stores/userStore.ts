import {defineStore} from "pinia";
import {ref} from "vue";
import axios from "axios";

export const useUserStore = defineStore("user", () => {
    const token = ref(localStorage.getItem("token") || "");
    const userInfo = ref({

    });
    const userTeams = ref([]);
    const hasValue = ref(false);

    function logout() {
        token.value = '';
        userInfo.value = null;
        hasValue.value = false;
        localStorage.removeItem('token');
    }
    // 定义一个函数来获取当前用户信息
    function getCurrentUser() {
        return {
            token: token.value,
            info: userInfo.value,
            teams: userTeams.value
        };
    }

    async function loadUserInfo() {
        const resOfUserInfo = await axios.get('/user/userInfo',{
            headers: {
                Authorization: token.value
            }
        })
        userInfo.value = resOfUserInfo.data;
        const resOfTeamsInfo = await axios.get('/user/teams', {
            headers: {
                Authorization: token.value
            }
        })
        userTeams.value = resOfTeamsInfo.data;
    }

    async function initialize() {
        if (!hasValue.value) {
            hasValue.value = true;
            await loadUserInfo();
        }
    }

    function getUserTeams() {
        return userTeams.value;
    }

    function setToken(theToken) {
        token.value = theToken;
    }

    return { token, userInfo, logout, getCurrentUser,
        userTeams, hasValue, getUserTeams, initialize,
        setToken};
})