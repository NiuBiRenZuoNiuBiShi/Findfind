<script setup lang="ts">
import {ref} from "vue";
import {ElMessage} from "element-plus";
import {useUserStore} from "../stores/userStore.ts";
import axios from "axios";
import router from "../router";

const userStore = useUserStore();
const formRef = ref(null);
const dataForm = ref({
  header: "",
  message: "",
  labels: [],
  teamId: "",
  needNum: 0,
  files: []
})
const rules = {
  header: [
    {required: true, message: "请输入标题", trigger: "blur"},
    {required: true, message: "请输入标题", trigger: "change"},
  ],
  message: [
    {required: true, message: "请输入正文", trigger: "blur"},
    {required: true, message: "请输入正文", trigger: "change"},
  ],
  teamId: [
    {required: true, message: "选择你的队伍", trigger: "blur"},
    {required: true, message: "选择你的队伍", trigger: "change"},
  ],
  needNum: [
    {required: true, message: "输入所需人数", trigger: "blur"},
    {required: true, message: "输入所需人数", trigger: "change"},
  ],
  labels: [
    {required: true, message: '请至少选择1个', trigger: 'change'},
    {required: true, message: "请至少选择1个", trigger: "blur"},
  ]
}
const userTeams = userStore.userTeams;

const inputLabel = ref("");
const addLabel = () => {
  if (inputLabel.value && !dataForm.value.labels.includes(inputLabel.value)) {
    dataForm.value.labels.push(inputLabel.value);
  }
}
const deleteLabel = (index: number) => {
  dataForm.value.labels.splice(index, 1);
}
const handExceed = () => {
  ElMessage.warning("最多五个文件")
}
const submitRecruit = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const recruitData = new FormData();
        recruitData.append("header", formRef.value.header);
        recruitData.append("message", formRef.value.message);
        recruitData.append("labels", formRef.value.labels);
        recruitData.append("teamId", formRef.value.teamId);
        recruitData.append("needNum", formRef.value.needNum);
        if (dataForm.value.files && dataForm.value.files.length > 0) {
          dataForm.value.files.forEach((f) => {
            recruitData.append("files", f.raw);
          })
        }

        const teamId = dataForm.value.teamId;
        const response = await axios.post(`/recruit/release/${teamId}`,
            recruitData,
            {
              headers: {
                "Content-Type": "multipart/form-data"
              }
            }
        );

        if (response.data.code === 1) {
          ElMessage.success("1s后返回")
          setTimeout(() => {
            router.push('/plaza')
          }, 1000);
        } else {
          console.log(response)
          ElMessage.error("提交失败");
        }
      } catch (error) {
        ElMessage.error(error)
      }
    }
  })
}
</script>

<template>
  <div class="createRecruitFormContainer">
    <el-card style="margin-top: 60px; height: 87vh">
      <el-header height="40px"> 创建招募信息</el-header>
      <el-form
          ref="formRef"
          :model="dataForm"
          :rules="rules"
          label-width="auto"
      >
        <el-form-item label="teamId" prop="teamId">
          <el-select
              v-model="dataForm.teamId"
              placeholder="选择你的队伍"
              size="large"
              clearable
          >
            <el-option v-for="team in userTeams"
                       :key="team.id"
                       :label="team.name"
                       :value="team.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="header" prop="header">
          <el-input v-model="dataForm.header" placeholder="输入标题"></el-input>
        </el-form-item>
        <el-form-item label="message" prop="message">
          <el-input type="textarea" :rows="10" placeholder="请输入描述信息" v-model="dataForm.message"></el-input>
        </el-form-item>
        <el-form-item
            label="inputLabel"
            prop="inputLabel"
            :max-length="5"
            :min-length="1"
        >
          <el-input v-model="inputLabel" placeholder="添加标签">
            <template #append>
              <el-button type="primary" @click="addLabel">添加</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="showLabels" prop="labels">
          <el-tag
              v-for="(tag, index) in dataForm.labels"
              :key="index"
              closable
              @close="deleteLabel(index)"
          >
            {{ tag }}
          </el-tag>
        </el-form-item>
        <el-form-item label="needNum" prop="needNum">
          <el-input-number v-model="dataForm.needNum" :min="1" :max="20" :precision="0"></el-input-number>
        </el-form-item>
        <el-form-item label="files">
          <el-upload
              v-model="dataForm.files"
              action=""
              :auto-upload="false"
              multiple
              :on-exceed="handExceed"
          >
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">
                最多上传5个文件，单个文件不超过10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item label=" ">
          <el-button @click="submitRecruit" type="primary" style="margin-top: -30px">创建</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.createRecruitFormContainer {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}
</style>