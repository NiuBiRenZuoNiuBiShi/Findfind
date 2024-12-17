<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { getUserInvites } from '../api/getUserInvites.ts';
import { ElMessage } from 'element-plus';

interface InviteRequest {
    id: number;
    userID: number;
    teamID: number;
    releaserID: number;
    header: string;
    message: string;
    response: string;
    status: number; // 0:未处理 1:已同意 2:已拒绝
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

const tableData = ref<PageInfo<InviteRequest> | null>(null);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

const fetchInvites = async () => {
    try {
        const res = await getUserInvites(currentPage.value, pageSize.value);
        console.log('Raw response:', res); // 打印出完整的响应对象
        if (res && res.data.data && 'list' in res.data.data && Array.isArray(res.data.data.list)) {
            tableData.value = res.data.data as PageInfo<InviteRequest>;
            total.value = res.data.data.total;
        } else {
            console.error('API response is not a PageInfo object:', res.data);
        }
    } catch (error) {
        console.error('Failed to fetch user list:', error);
        ElMessage.error('获取数据失败');
    }
};

onMounted(() => {
    fetchInvites();
});

const handlePageChange = (newPage: number) => {
    currentPage.value = newPage;
    fetchInvites();
};
</script>
<template>
        <div class="common-layout">
            <el-container>
                <el-header></el-header>
                <el-main>
                    <el-table :data="tableData?.list" stripe style="width: 100%">
                        <el-table-column prop="teamID" label="队伍id" width="200" style="height: 150px;">
                            <template #default="scope">
                                {{ scope.row.teamID || 'null' }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="message" label="内容" width="380">
                            <template #default="scope">
                                {{ scope.row.message || 'null' }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="status" label="处理结果" width="180" style="height: 150px;">
                            <template #default="scope">
                                {{ scope.row.status === 0 ? '未处理' : scope.row.status === 1 ? '已同意' : scope.row.status === 2 ? '已拒绝' : 'null' }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="response" label="回复" style="height: 150px;">
                            <template #default="scope">
                                {{ scope.row.response || 'null' }}
                            </template>
                        </el-table-column>
                        <!-- 其他列 -->
                    </el-table>
                </el-main>
            </el-container>
            <el-pagination
                v-if="total > 0"
                layout="total, prev, pager, next"
                :total="total"
                :current-page="currentPage"
                :page-size="pageSize"
                @current-change="handlePageChange"
                class="pagination">
            </el-pagination>
        </div>
</template>

<style scoped>
.common-layout {
    padding: 20px;
}
.pagination {
    margin-top: 20px;
    text-align: center;
}
</style>