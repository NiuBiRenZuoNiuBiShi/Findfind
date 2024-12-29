<template>
    <div class="seeker-plaza-container">
        <div class="table-container">
            <el-table
                :data="seekers"
                style="width: 100%"
                stripe
                border
                class="custom-table"
            >
                <el-table-column type="selection" width="75" align="center" />
                <el-table-column prop="header" label="标题" min-width="150" align="left">
                    <template #default="scope">
                        <div class="title-cell">
                            <span class="title-text">{{ scope.row.header }}</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="position" label="位置" width="180" align="center">
                    <template #default="scope">
                        <span class="position-text">{{ scope.row.position }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="label" label="标签" min-width="150" align="left">
                    <template #default="scope">
                        <div class="tags-wrapper">
                            <el-tag
                                v-for="(tag, index) in scope.row.labels"
                                :key="tag"
                                :class="['table-tag', `tag-${index % 5}`]"
                                size="small"
                            >
                                {{ tag }}
                            </el-tag>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" width="150" align="center">
                    <template #default="scope">
                        <span class="time-text">{{ formatDateTime(scope.row.createTime) }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="150" align="center">
                    <template #default="scope">
                        <el-button
                            type="primary"
                            link
                            @click="seeDetail(scope.row)"
                            class="detail-button"
                        >
                            查看详情
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!-- 详情弹窗 -->
        <el-dialog
            v-model="seekerDetailsDialogVisible"
            title="求职者详情"
            width="60%"
            class="custom-dialog"
            destroy-on-close
        >
            <template v-if="currentSeeker">
                <el-descriptions :column="2" border class="custom-descriptions">
                    <el-descriptions-item label="标题" :span="2">
                        <span class="description-title">{{currentSeeker.header}}</span>
                    </el-descriptions-item>
                    <el-descriptions-item label="位置" :span="2">
                        <span class="position-text">{{currentSeeker.position}}</span>
                    </el-descriptions-item>
                    <el-descriptions-item label="详细信息" :span="2">
                        <div class="description-content">
                            {{currentSeeker.message}}
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="附件" :span="2">
                        <div class="attachments-container">
                            <template v-if="currentSeeker.files && currentSeeker.files.length > 0">
                                <el-button
                                    v-for="(file, index) in currentSeeker.files"
                                    :key="index"
                                    type="primary"
                                    link
                                    @click="downloadFile(file)"
                                    class="file-button"
                                >
                                    <el-icon class="file-icon"><Document /></el-icon>
                                    {{ file.name }}
                                </el-button>
                            </template>
                            <el-empty v-else description="暂无附件" />
                        </div>
                    </el-descriptions-item>
                </el-descriptions>
            </template>
        </el-dialog>
    </div>
</template>
<script setup>
import {ref, onMounted} from 'vue'
import {ElMessage} from 'element-plus'
import axios from 'axios'
import {downloadUtils} from "../api/downloadUtils.ts";
import { Document} from '@element-plus/icons-vue'
import  {getUserSeekers} from "../api/getUserSeekers.ts";
const seekers = ref([]);
const total = ref(0);
const seekerDetailsDialogVisible = ref(false);
const currentSeeker = ref({})
const searchSeekers = async () => {
    try {
        const res = await getUserSeekers();
        console.log(res.data.data);
        if (res.data.code === 1) {
            const result = res.data.data;
            if (!Array.isArray(res.data.data)){
                seekers.value = null;
            } else {
                seekers.value =  res.data.data
                console.log(seekers);
            }
        } else {
            ElMessage.error(res.data.message)
        }
    } catch (error) {
        console.log(error)
    }
}
const formatDateTime = (dateTime) => {
    return dateTime ? new Date(dateTime).toLocaleDateString() : ' ';
}
const seeDetail = async (seeker) => {
    currentSeeker.value = seeker;
    seekerDetailsDialogVisible.value = true;
    try {
        const params = new URLSearchParams()
        params.append("seekerId",seeker.id);
        const token = localStorage.getItem('token');
        const res = await axios.get('plaza/seeker/files', {
            params,
            headers: {
                Authorization: token
            }
        })
        console.log(res);
        if (res.data.code === 1) {
            const result = res.data.data
            if (result == null) {
                ElMessage.error("获取文件失败")
            } else {
                currentSeeker.value.files = result.map(item => {
                    const decodedData = atob(item.fileData); // 解码 Base64 数据
                    // 创建二进制数组
                    const byteArray = new Uint8Array(decodedData.length);
                    for (let i = 0; i < decodedData.length; i++) {
                        byteArray[i] = decodedData.charCodeAt(i);
                    }

                    const blob = new Blob([byteArray], {type: 'application/octet-stream'})
                    return {
                        data: blob,
                        name: item.fileName,
                    }
                })
            }
        }
    } catch (error) {
        console.log(error)
    }
}
const downloadFile = (file) => {
    console.log(file)
    downloadUtils.generateDownloadLink(file.data, file.name);
}
onMounted(() => {
    searchSeekers();
});
</script>

<style scoped>
.seeker-plaza-container {
    padding: 24px;
    background-color: #f5f7fa;
    min-height: calc(100vh - 48px);
    margin-top: 64px;
}

.search-section {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    margin-bottom: 24px;
}

.label-input-item {
    margin-bottom: 0;
}

.label-input {
    width: 400px;
}

.label-input :deep(.el-input__wrapper) {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.dynamic-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    padding: 8px 0;
}

.custom-tag {
    padding: 0 12px;
    height: 32px;
    line-height: 30px;
    border-radius: 16px;
    font-weight: 500;
}

.tag-0 { background-color: #e6f4ff; color: #1677ff; border-color: #1677ff; }
.tag-1 { background-color: #f6ffed; color: #52c41a; border-color: #52c41a; }
.tag-2 { background-color: #fff7e6; color: #fa8c16; border-color: #fa8c16; }
.tag-3 { background-color: #fff1f0; color: #f5222d; border-color: #f5222d; }
.tag-4 { background-color: #f9f0ff; color: #722ed1; border-color: #722ed1; }

.search-button {
    margin-left: 16px;
    padding: 0 24px;
}

.table-container {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    margin-bottom: 24px;
}

.custom-table {
    margin: 16px 0;
}

.title-cell {
    padding: 8px 0;
}

.title-text {
    font-weight: 500;
    color: #2c3e50;
}

.position-text {
    color: #666;
    font-size: 14px;
}

.tags-wrapper {
    display: flex;
    flex-wrap: wrap;
    gap: 4px;
}

.table-tag {
    border-radius: 12px;
}

.time-text {
    color: #666;
    font-size: 14px;
}

.detail-button {
    font-weight: 500;
}

.actions-container {
    margin: 24px 0;
    display: flex;
    justify-content: center;
}

.invite-button {
    padding: 0 32px;
    height: 40px;
}

.pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 24px;
}

.custom-pagination {
    padding: 16px 0;
}

.custom-dialog :deep(.el-dialog__body) {
    padding: 24px;
}

.description-title {
    font-size: 16px;
    font-weight: 500;
    color: #2c3e50;
}

.description-content {
    white-space: pre-wrap;
    line-height: 1.6;
    color: #666;
    padding: 12px;
    background: #f8f9fa;
    border-radius: 4px;
}

.attachments-container {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    padding: 12px;
}

.file-button {
    display: flex;
    align-items: center;
    gap: 8px;
}

.file-icon {
    font-size: 16px;
}

.dialog-footer {
    margin-top: 24px;
    text-align: right;
}
</style>