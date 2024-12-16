<template>
    <button class="back-button" @click="goBack">上一页</button>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();
const goBack = () => {
    router.go(-1);
};
</script>

<style scoped>
.back-button {
    padding: 10px; /* 由于是圆形按钮，padding调整为均匀值 */
    width: 60px; /* 设置宽度，使按钮为圆形 */
    height: 60px; /* 设置高度，使按钮为圆形 */
    border: none;
    border-radius: 50%; /* 设置为圆形 */
    background-color: rgba(154, 150, 150, 0.2); /* 初始颜色为白色系，透明度较低 */
    color: #333; /* 文字颜色为深灰色 */
    cursor: pointer;
    transition: all 0.2s ease;
    position: fixed;
    bottom: 220px; /* 距离窗口底部20px */
    right: 50px; /* 距离窗口右侧20px */
    box-shadow: none; /* 初始状态无阴影 */
    display: flex;
    align-items: center;
    justify-content: center;
}

.back-button:hover {
    background-color: rgb(117, 114, 114); /* hover时颜色变为不透明白色 */
    color: #f8f6f6; /* 文字颜色为深灰色 */
    transform: scale(1.1); /* hover时放大 */
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1); /* hover时添加阴影 */
}

.back-button:active {
    transform: scale(0.9); /* 按下时缩小 */
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2); /* 按下时阴影更深 */
}
</style>