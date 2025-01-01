import {defineStore} from "pinia";
import {ref} from "vue";
import {instance} from "../api/user.ts";

export const useUserStore = defineStore("user", () => {
    const token = ref(localStorage.getItem("token") || "");
    const userInfo = ref({});
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
        const resOfUserInfo = await instance.get('/user/userInfo', {
            headers: {
                Authorization: localStorage.getItem("token"),
            }
        })
        userInfo.value = resOfUserInfo.data.data;
        const resOfTeamsInfo = await instance.get('/teams/getByUser', {
            headers: {
                Authorization: localStorage.getItem("token"),
            }
        })
        userTeams.value = resOfTeamsInfo.data.data;
        console.log(userTeams.value);
    }

    async function initialize() {
        if (!hasValue.value && localStorage.getItem('token') != null) {
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

    function addTeam(team) {
        userTeams.value.push(team);
    }

    function removeTeam(team) {
        userTeams.value.splice(team, 1);
    }

    return {
        token, userInfo, logout, getCurrentUser,
        userTeams, hasValue, getUserTeams, initialize,
        setToken, loadUserInfo, addTeam, removeTeam
    };
})