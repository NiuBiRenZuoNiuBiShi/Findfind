<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { getUserJoins } from '../api/getUserJoins.ts';
import { getTeamInfo as apiGetTeamInfo } from "../api/getTeamInfo.ts";
import { ElMessage } from 'element-plus';

interface JoinRequest {
    id: number;
    userId: number;
    teamId: number;
    message: string;
    response: string;
    status: number; // 0:未处理 1:已同意 2:已拒绝
    createTime: string;
    updateTime: string;
    teamName?: string; // 队伍名称
    teamDescription?: string; // 队伍描述
}

interface PageInfo<T> {
    total: number;
    list: T[];
    pageNum: number;
    pageSize: number;
    size: number;
}

const tableData = ref<PageInfo<JoinRequest> | null>(null);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

// 根据队伍ID获取队伍信息
const getTeamInfo = async (teamId: number) => {
    try {
        const response = await apiGetTeamInfo(teamId);
        console.log('Raw response3:', response); // 打印出完整的响应对象
        if (response && response.data.data) {
            console.log('Raw response:', response.data.data); // 打印出完整的响应对象
            return response.data.data; // 确保后端返回的数据结构中包含name和description字段
        } else {
            ElMessage.error('获取队伍信息失败');
            return null;
        }
    } catch (error) {
        console.error('Failed to fetch team info:', error);
        ElMessage.error('获取队伍信息失败');
        return null;
    }
};

const fetchJoins = async () => {
    try {
        const res = await getUserJoins(currentPage.value, pageSize.value);
        console.log('Raw response:', res); // 打印出完整的响应对象
        if (res && res.data.data && 'list' in res.data.data && Array.isArray(res.data.data.list)) {
            tableData.value = res.data.data as PageInfo<JoinRequest>;
            total.value = res.data.data.total;
            await Promise.all(tableData.value.list.map(async (join) => {
                const teamInfo = await getTeamInfo(join.teamId);
                if (teamInfo) {
                    join.teamName = teamInfo.name;
                    join.teamDescription = teamInfo.description;
                }
            }));
        } else {
            console.error('API response is not a PageInfo object:', res.data);
        }
    } catch (error) {
        console.error('Failed to fetch user list:', error);
        ElMessage.error('获取数据失败');
    }
};

onMounted(() => {
    fetchJoins();
});

const handlePageChange = (newPage: number) => {
    currentPage.value = newPage;
    fetchJoins();
};
</script>
<template>
    <div class="common-layout">
        <el-container>
            <el-header></el-header>
            <el-main>
                <el-table :data="tableData?.list" stripe style="width: 100%">
                    <el-table-column prop="teamId" label="队伍id" width="200"></el-table-column>
                    <el-table-column prop="teamName" label="队伍名称" width="180">
                        <template #default="scope">
                            {{ scope.row.teamName || '未知队伍' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="teamDescription" label="队伍描述" width="280">
                        <template #default="scope">
                            {{ scope.row.teamDescription || '无描述' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="message" label="内容" width="380"></el-table-column>
                    <el-table-column prop="status" label="处理结果" width="180">
                        <template #default="scope">
                            <span :class="'status-box status-' + scope.row.status">
                                {{ scope.row.status === 0 ? '未处理' : scope.row.status === 1 ? '已同意' : scope.row.status === 2 ? '已拒绝' : 'null' }}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="response" label="回复" width="200"></el-table-column>
                    <!-- 其他列 -->
                </el-table>
            </el-main>
            <el-pagination
                    v-if="total > 0"
                    layout="total, prev, pager, next"
                    :total="total"
                    :current-page="currentPage"
                    :page-size="pageSize"
                    @current-change="handlePageChange"
                    class="pagination">
            </el-pagination>
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
}/* 定义一个 CSS 类，用于设置带颜色的方框 */
.status-box {
    display: inline-block;
    padding: 2px 8px;
    border-radius: 4px;
    color: #fff; /* 文字颜色 */
    font-weight: bold;
}

/* 定义不同状态的颜色 */
.status-0 {
    background-color: #f56c6c; /* 未处理，红色 */
}
.status-1 {
    background-color: #67c23a; /* 已同意，绿色 */
}
.status-2 {
    background-color: #e6a23c; /* 已拒绝，橙色 */
}
</style>