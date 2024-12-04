<script lang="ts" setup>
import {ref} from 'vue'

const tableData = ref([
  {
    "name": "test",
    "type": "test",
    "description": "test",
    "id": 1,
    "label": "test",
    "position": "test",
    "status": "test",
    "createTime": "2021-08-17T07:00:00.000+00:00",
    "updateTime": "2021-08-17T07:00:00.000+00:00"
  },
])
//获取队伍列表
import {getTeamList} from "../api/getTeamList.ts";

const getTeamListData = async () => {
  const res = await getTeamList()
  tableData.value = res.data.data
  console.log(tableData.value)
}
getTeamListData()
//获取队伍列表

</script>
<template>
  <div class="common-layout">
    <el-container>
      <el-header>

      </el-header>
      <el-main>
        <el-table :data="tableData" stripe style="width: 100%">
          <el-table-column prop="name" label="队伍名称" width="180"/>
          <el-table-column prop="type" label="队伍类型" width="180"/>
          <el-table-column prop="description" label="描述"/>
        </el-table>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>

</style>