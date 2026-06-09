<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6"><el-card><div class="stat-number">{{ projectStore.projects.length }}</div><div>项目总数</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-number">{{ taskStore.tasks.length }}</div><div>任务总数</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-number">{{ taskStore.completedTasks.length }}</div><div>已完成</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-number">{{ taskStore.inProgressTasks.length }}</div><div>进行中</div></el-card></el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useProjectStore } from '@/stores/project'
import { useTaskStore } from '@/stores/task'

const projectStore = useProjectStore()
const taskStore = useTaskStore()

onMounted(async () => {
  await projectStore.fetchProjects()
  if (projectStore.projects[0]) {
    await taskStore.fetchTasks(projectStore.projects[0].id)
  }
})
</script>

<style scoped>
.stat-number {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
}
</style>