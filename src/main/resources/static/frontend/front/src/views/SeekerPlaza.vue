<template>
  <div class="seeker-plaza-container" >
    <el-form :inline="true" class="search-form" style="margin-top: 75px;margin-bottom: -10px">
      <el-form-item label="添加标签">
        <el-input
            v-model="inputLabel"
            placeholder="添加标签"
            size="small"
            style="width: 200px;"
            @keyup.enter="addLabel">
          <template #append>
            <el-button @click="addLabel">添加</el-button>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item>
        <div class="dynamic-tags">
          <el-tag
              v-for="(tag, index) in selectedLabels"
              :key="tag"
              closable
              @close="removeLabel(index)"
          >
            {{ tag }}
          </el-tag>
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="searchSeekers"></el-button>
      </el-form-item>
    </el-form>


    <el-table
        :data="seekers"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        stripe
        border
    >
      <el-table-column type="selection" width="75"></el-table-column>
      <el-table-column prop="header" label="标题" width="439px" align="center"></el-table-column>
      <el-table-column prop="postion" label="位置" width="250px" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="250" align="center"></el-table-column>
      <el-table-column prop="label" label="标签" width="250px" header-align="center">
        <template #default="scope">
          <el-tag
              v-for="tag in scope.row.label"
              :key="tag"
              size="small"
              style="margin-right: 5px"
          >
            {{ tag }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="seeDetails" prop="seekerId" width="200" align="center">
        <template #default="scope">
          <el-button
              type="text"
              size="small"
              @click="seeDetail(scope.row)"
          >
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="actions-container" v-if="seekers.length > 0">
      <el-button
          type="primary"
          @click="goToInviteRequest"
          :disabled="seekers.length !== 1">
      </el-button>
    </div>

    <el-pagination
        layout="total, prev, pager, next"
        :total="seekers.length"
        :current-page="currentPage"
        :page-size="pageSize"
        @current-change="handlePageChange"
        class="pagination">
    </el-pagination>

    <el-dialog v-if="currentSeeker">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="Title" :span="2">
          {{ currentSeeker.header }}
        </el-descriptions-item>
        <el-descriptions-item label="Message" :span="2">
          {{ currentSeeker.message }}
        </el-descriptions-item>

        <el-descriptions-item label="Attachments" :span="2">
          <ul v-if="currentSeeker.files.length > 0">
            <li v-for="(file, index) in currentSeeker.files" :key="index">
              <button @click="downloadFile(file)">
                {{ file.name }}
              </button>
            </li>
          </ul>
          <p v-else>暂无附件</p>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {ElMessage} from 'element-plus'
import axios from 'axios'
import router from "../router";
import {downloadUtils} from "../api/downloadUtils.ts";

const inputLabel = ref('');
const selectedLabels = ref([]);
const seekers = ref([]);
const selectedSeeker = ref([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);
const seekerDetailsDialogVisible = ref(false);
const currentSeeker = ref(null)

const addLabel = () => {
  const label = inputLabel.value;
  if (label && !selectedLabels.value.includes(label)) {
    selectedLabels.value.push(label);
    inputLabel.value = '';
  }
}

const removeLabel = index => {
  selectedLabels.value.splice(index, 1);
}

const searchSeekers = async () => {
  try {
    const res = await axios.get('plaza/seeker', {
      params: {
        labels: selectedLabels.value,
        page: currentPage.value,
        pageSize: pageSize.value,
      }
    })

    if (res.data.code === 1) {
      seekers.value = res.data.data
      total.value = res.data.data.total || seekers.value.length
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
    console.log(error)
    console.log(error)
  }
}

const handleSelectionChange = (selected) => {
  selectedSeeker.value = selected;
}

const handlePageChange = (page) => {
  currentPage.value = page;
  searchSeekers();
}

const formatDateTime = (dateTime) => {
  return dateTime ? new Data(dateTime).toLocaleDateString() : ' ';
}

const seeDetail = (seeker) => {
  currentSeeker.value = seeker;
  seekerDetailsDialogVisible.value = true;
}

const downloadFile = (file) => {
  downloadUtils.generateDownloadLink(file.data, file.name);
}

const goToInviteRequest = () => {
  if (selectedSeeker.value.length === 0) {
    const seekerId = selectedSeeker.value[0].id;
    router.push(`/plaza/seeker/${seekerId}`);
  }
}
</script>

<style scoped>
.seeker-plaza-container {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.dynamic-tags {
  display: flex;
  gap: 10px;
  margin-left: 10px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.actions-container {
  margin-top: 20px;
  text-align: center;
}
</style>