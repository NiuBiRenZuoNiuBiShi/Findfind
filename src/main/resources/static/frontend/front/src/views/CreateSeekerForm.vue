<script setup lang="ts">
import {ref} from "vue";
import {ElMessage} from "element-plus";
import axios from "axios";
import router from "../router";
import {useUserStore} from "../stores/userStore.ts";

const userStore = useUserStore()
const formRef = ref(null);
const dataForm = ref({
  header: "",
  message: "",
  labels: [],
  files: [],
  position: ""
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
  labels: [
    {required: true, message: '请至少选择1个', trigger: 'change'},
    {required: true, message: "请至少选择1个", trigger: "blur"},
  ],
  position: [
    {required: true, message: '请输入地址', trigger: 'change'},
    {required: true, message: '请输入地址', trigger: 'change'},
  ]
}

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
        console.log(dataForm.value);
        const seekerData = new FormData();
        seekerData.append("header", dataForm.value.header);
        seekerData.append("message", dataForm.value.message);
        seekerData.append("position", dataForm.value.position);
        if (dataForm.value.labels && dataForm.value.labels.length > 0) {
          dataForm.value.labels.forEach((label) => {
            seekerData.append("labels", label);
          })
        }
        if (dataForm.value.files && dataForm.value.files.length > 0) {
          console.log("test")
          dataForm.value.files.forEach((f) => {
            seekerData.append("files", f.raw || f);
          })
        }

        for (let pair of seekerData.entries()) {
          console.log(pair[0] + ': ', pair[1]);
        }

        const response = await axios.post(`/plaza/seeker`,
            seekerData,
            {
              headers: {
                Authorization: userStore.token,
                "Content-Type": "multipart/form-data"
              }
            }
        );

        if (response.data.code === 1) {
          ElMessage.success("1s后返回")
          setTimeout(() => {
            router.push('/seekerPlaza')
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
const quit = () => {
  router.push('/plaza')
}

const handleFileChange = (file, fileList) => {
  dataForm.value.files = fileList;
}
</script>

<template>
  <div class="createSeekerFormContainer">
    <el-card style="margin-top: 60px; height: 87vh">
      <el-header height="40px"> 创建招募信息</el-header>
      <el-form
          ref="formRef"
          :model="dataForm"
          :rules="rules"
          label-width="auto"
      >
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
        <el-form-item label="position" prop="position">
          <el-input v-model="dataForm.position" placeholder="请输入地址"> </el-input>
        </el-form-item>
        <el-form-item label="files">
          <el-upload
              action=""
              :auto-upload="false"
              multiple
              :on-exceed="handExceed"
              :on-change="handleFileChange"
              :file-list="dataForm.files"
              :limit="5"
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
          <el-button @click="quit" type="danger" style="margin-top: -30px">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.createSeekerFormContainer {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}
</style>