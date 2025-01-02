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
  position: "",
  fileNames: []
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
          dataForm.value.files.forEach((f) => {
            seekerData.append("files", f.raw || f);
            //console.log(f.raw)
          })
        }
        if (dataForm.value.fileNames && dataForm.value.fileNames.length > 0) {
          dataForm.value.fileNames.forEach((f) => {
            seekerData.append("fileNames", f);
          })
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
  router.push('/seekerPlaza')
}

const handleFileChange = (file, fileList) => {
  dataForm.value.files = fileList.map(f => f.raw)
  dataForm.value.fileNames = fileList.map(f => f.name)
}
</script>

<template>
  <div class="createSeekerFormContainer">
    <div class="form-wrapper">
      <div class="form-header">
        <div class="header-title">创建招募信息</div>
      </div>

      <el-form
          ref="formRef"
          :model="dataForm"
          :rules="rules"
          label-width="120px"
          class="seeker-form"
      >
        <el-form-item label="标题" prop="header">
          <el-input
              v-model="dataForm.header"
              placeholder="请输入标题"
              class="custom-input"
          />
        </el-form-item>

        <el-form-item label="描述信息" prop="message">
          <el-input
              type="textarea"
              :rows="10"
              placeholder="请输入描述信息"
              v-model="dataForm.message"
              class="custom-textarea"
          />
        </el-form-item>

        <el-form-item label="标签" prop="inputLabel">
          <div class="label-input-container">
            <el-input
                v-model="inputLabel"
                placeholder="添加标签"
                class="label-input"
            >
              <template #append>
                <el-button type="primary" @click="addLabel">添加</el-button>
              </template>
            </el-input>
          </div>
        </el-form-item>

        <el-form-item label="已添加标签" prop="labels">
          <div class="tags-container">
            <el-tag
                v-for="(tag, index) in dataForm.labels"
                :key="index"
                closable
                @close="deleteLabel(index)"
                class="custom-tag"
            >
              {{ tag }}
            </el-tag>
          </div>
        </el-form-item>

        <el-form-item label="地址" prop="position">
          <el-input
              v-model="dataForm.position"
              placeholder="请输入地址"
              class="custom-input"
          />
        </el-form-item>

        <el-form-item label="附件">
          <el-upload
              action=""
              :auto-upload="false"
              multiple
              :on-exceed="handExceed"
              :on-change="handleFileChange"
              :file-list="dataForm.files"
              :limit="5"
              class="upload-section"
          >
            <el-button type="primary" class="upload-button">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">
                最多上传5个文件，单个文件不超过10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item class="dialog-footer">
          <el-button @click="quit" plain>取消</el-button>
          <el-button type="primary" @click="submitRecruit">创建</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.createSeekerFormContainer {
  max-width: 800px;
  margin: 60px auto;
  padding: 20px;
}

.form-wrapper {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.form-header {
  padding: 16px 24px;
  border-bottom: 1px solid #ebeef5;
}

.header-title {
  font-size: 20px;
  color: #333;
  font-weight: 500;
  line-height: 1.5;
  text-align: center;
}

.seeker-form {
  padding: 24px;
  margin: 0 auto;
}

.seeker-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.seeker-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #333;
  font-size: 14px;
  line-height: 1.5;
  padding-right: 20px;
}

.custom-input,
.custom-textarea,
.label-input-container,
.upload-section {
  width: 100%;
  max-width: 500px;
}

.seeker-form :deep(.el-input__wrapper),
.seeker-form :deep(.el-textarea__inner) {
  box-shadow: none;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  transition: all 0.3s;
}

.seeker-form :deep(.el-input__wrapper:hover),
.seeker-form :deep(.el-textarea__inner:hover) {
  border-color: #409eff;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  max-width: 500px;
}

.custom-tag {
  margin-right: 8px;
  margin-bottom: 8px;
}

.upload-section :deep(.el-upload__tip) {
  color: #909399;
  font-size: 13px;
  margin-top: 8px;
}

.dialog-footer {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
  text-align: right;
}

.dialog-footer :deep(.el-button) {
  padding: 9px 20px;
  margin-left: 12px;
}

:deep(.el-form-item__content) {
  justify-content: flex-start;
  margin-left: 0 !important;
}
</style>