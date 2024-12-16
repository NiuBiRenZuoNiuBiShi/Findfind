<script lang="ts" setup>
import {ref,onMounted} from 'vue'
import {getUserInvites} from "../api/getUserInvites.ts";
interface InviteRequest {
    id:number;
    userID:number;
    teamID:number
    releaserID:number;
    header:number;
    message:string;
    response:string;
    status:number;//0:未处理 1:已同意 2:已拒绝
    createTime:string;
    updateTime:string;
}
interface PageInfo<T>{
    total: number;
    list: T[];
    pageNum: number;
    pageSize: number;
    size: number;
}
const tableData = ref<PageInfo<InviteRequest> | null>(null);
// 在 setup 函数的顶部注册 onMounted 钩子
onMounted(async () => {
    try {
        const res = await getUserInvites();
        console.log('Raw response:', res); // 打印出完整的响应对象
        if (res && res.data.data && 'list' in res.data.data && Array.isArray(res.data.data.list)) {
            tableData.value = res.data.data as PageInfo<InviteRequest>;
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
                    <el-table-column prop="teamid" label="队伍id" width="100" style="height: 150px;" >
                        <template #default="scope">
                            {{ scope.row.id||'null' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="message" label="内容" width="180">
                        <template #default="scope">
                            {{ scope.row.message || 'null' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="response" label="回复" width="180" style="height: 150px;" >
                        <template #default="scope">
                            {{ scope.row.response || 'null' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="status" label="处理结果" width="180" style="height: 150px;" >
                        <template #default="scope">
                            {{ scope.row.status === 0 || scope.row.status === 1 || scope.row.status === 2 ? scope.row.status : 'null' }}
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