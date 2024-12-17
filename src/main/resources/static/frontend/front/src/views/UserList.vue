<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { getUserList } from '../api/getUserList';
import { ElMessage } from 'element-plus';

interface User {
    username: string;
    nickName: string;
    id: number;
    email: string;
    phone: string;
    biology: string;
    position: string;
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
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

const fetchUsers = async () => {
    try {
        const res = await getUserList(currentPage.value, pageSize.value);
        console.log('Raw response:', res); // 打印出完整的响应对象
        if (res && res.data.data && 'list' in res.data.data && Array.isArray(res.data.data.list)) {
            tableData.value = res.data.data as PageInfo<User>;
            total.value = res.data.data.total;
        } else {
            console.error('API response is not a PageInfo object:', res.data);
        }
    } catch (error) {
        console.error('Failed to fetch user list:', error);
        ElMessage.error('获取用户列表失败');
    }
};

onMounted(() => {
    fetchUsers();
});

const handlePageChange = (newPage: number) => {
    currentPage.value = newPage;
    fetchUsers();
};
</script>
<template>
        <div class="common-layout">
            <el-container>
                <el-header></el-header>
                <el-main>
                    <el-table :data="tableData?.list" stripe style="width: 100%">
                        <el-table-column prop="id" label="ID" width="100" style="height: 150px;">
                            <template #default="scope">
                                {{ scope.row.id || 'null' }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="username" label="姓名" width="180">
                            <template #default="scope">
                                {{ scope.row.username || 'null' }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="nickName" label="昵称" width="180" style="height: 150px;">
                            <template #default="scope">
                                {{ scope.row.nickName || 'null' }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="position" label="地址" width="180" style="height: 150px;">
                            <template #default="scope">
                                {{ scope.row.position || 'null' }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="phone" label="电话" width="180" style="height: 150px;">
                            <template #default="scope">
                                {{ scope.row.phone || 'null' }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="biology" label="自我介绍" style="height: 150px;">
                            <template #default="scope">
                                {{ scope.row.biology || 'null' }}
                            </template>
                        </el-table-column>
                        <!-- 其他列 -->
                    </el-table>
                    <el-pagination
                        v-if="total > 0"
                        layout="total, prev, pager, next"
                        :total="total"
                        :current-page="currentPage"
                        :page-size="pageSize"
                        @current-change="handlePageChange"
                        class="pagination">
                    </el-pagination>
                </el-main>
            </el-container>
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