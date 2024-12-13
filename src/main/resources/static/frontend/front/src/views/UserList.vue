<script lang="ts" setup>
import {ref,onMounted} from 'vue'
import { getUserList } from '../api/getUserList';
interface User {
    username: string;
    nick_name: string;
    id: number;
    email: string;
    phone: string;
    biology: string;
    postion: string;
    status: string;
    createTime: string;
    updateTime: string;
}

interface PageInfo<T> {
    total: number;
    list: T[];
    pageNum: number;
    pageSize: number;
    size: number;
}
const tableData = ref<PageInfo<User> | null>(null);
// 在 setup 函数的顶部注册 onMounted 钩子
onMounted(async () => {
    try {
        const res = await getUserList();
        console.log('Raw response:', res); // 打印出完整的响应对象

        if (res && res.data.data && 'list' in res.data.data && Array.isArray(res.data.data.list)) {
            tableData.value = res.data.data as PageInfo<User>;
        } else {
            console.error('API response is not a PageInfo object:', res.data);
        }
    } catch (error) {
        console.error('Failed to fetch user list:', error);
    }
});
</script>
<template>
    <div class="common-layout">
        <el-container>
            <el-header></el-header>
            <el-main>
                <el-table :data="tableData?.list" stripe style="width: 100%">
                    <el-table-column prop="username" label="姓名" width="180">
                        <template #default="scope">
                            {{ scope.row.username || 'null' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="nick_name" label="昵称" width="180" style="height: 150px;" >
                        <template #default="scope">
                            {{ scope.row.nick_name || 'null' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="postion" label="地址" width="180" style="height: 150px;" >
                        <template #default="scope">
                            {{ scope.row.postion || 'null' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="phone" label="电话" width="180" style="height: 150px;">
                        <template #default="scope">
                            {{ scope.row.phone || 'null' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="biology" label="自我介绍"  style="height: 150px;">
                        <template #default="scope">
                            {{ scope.row.biology || 'null' }}
                        </template>
                    </el-table-column>
                    <!-- 其他列 -->
                </el-table>
            </el-main>
        </el-container>
    </div>
</template>

<style scoped>

</style>