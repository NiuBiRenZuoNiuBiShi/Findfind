<template>
  <div class="seeker-plaza-container">
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
        <el-button type="primary" @click="searchSeekers">搜索</el-button>
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
      <el-table-column prop="position" label="位置" width="250px" align="center"></el-table-column>
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
              type="primary"
              size="small"
              @click="seeDetail(scope.row)"
          >
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="actions-container" v-if="seekers && seekers.length > 0">
      <el-button
          type="primary"
          @click="goToInviteRequest"
          :disabled="seekers.length !== 1">
      </el-button>
    </div>

    <el-pagination v-if="seekers"
                   layout="total, prev, pager, next"
                   :total="seekers.length"
                   :current-page="currentPage"
                   :page-size="pageSize"
                   @current-change="handlePageChange"
                   class="pagination">
    </el-pagination>

    <el-dialog title="Seeker Details"
               v-model="seekerDetailsDialogVisible"
               :width="'60%'"
               :center="true" style="margin-top: 220px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="Title" :span="2">
          {{ currentSeeker.header }}
        </el-descriptions-item>
        <el-descriptions-item label="Message" :span="2">
          {{ currentSeeker.message }}
        </el-descriptions-item>

        <el-descriptions-item label="Attachments" :span="2">
          <ul v-if="currentSeeker.files">
            <li v-for="(file, index) in currentSeeker.files" :key="index">
              <button @click="downloadFile(file)">
                {{ file.name }}
              </button>
            </li>
          </ul>
          <p v-else>暂无附件</p>
        </el-descriptions-item>
      </el-descriptions>
      <el-button @click="goToInvite" style="margin-top: 20px"> 邀请</el-button>
      <el-button @click="quitDetails" style="margin-top: 20px">关闭</el-button>
    </el-dialog>


    <el-dialog title="Invite" v-model="inviteFormVisible" :width="'50%'" :center="true">
      <InviteRequestForm v-model:visible="inviteFormVisible" :seekerID="currentSeeker.id"
                         style="margin-top: -60px"/>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {ElMessage} from 'element-plus'
import axios from 'axios'
import {downloadUtils} from "../api/downloadUtils.ts";
import InviteRequestForm from "./InviteRequestForm.vue";
import {useUserStore} from "../stores/userStore.ts";

const inputLabel = ref('');
const selectedLabels = ref([]);
const seekers = ref([]);
const selectedSeeker = ref([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);
const seekerDetailsDialogVisible = ref(false);
const currentSeeker = ref({})
const inviteFormVisible = ref(false);
const userStore = useUserStore();

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
    const params = new URLSearchParams();
    selectedLabels.value.forEach(item => {
      params.append("labels", item);
    });
    params.append("page", currentPage.value);
    params.append("size", pageSize.value);
    const res = await axios.get('/plaza/seeker', {
      params,
      headers: {
        Authorization: userStore.token
      }
    })

    if (res.data.code === 1) {
      const result = res.data.data
      if (result.list == null) {
        ElMessage.info("无数据")
        seekers.value = null;
      } else {
        seekers.value = result.list
        total.value = res.data.data.total || seekers.value.length
      }
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
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
  console.log(inviteFormVisible.value)
  currentSeeker.value = seeker;
  seekerDetailsDialogVisible.value = true;
}

const downloadFile = (file) => {
  downloadUtils.generateDownloadLink(file.data, file.name);
}

const goToInviteRequest = () => {
  inviteFormVisible.value = true;
}

const goToInvite = () => {
  inviteFormVisible.value = true;
}

const quitDetails = () => {
  seekerDetailsDialogVisible.value = false;
  currentSeeker.value = null;
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