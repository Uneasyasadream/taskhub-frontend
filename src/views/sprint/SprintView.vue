<template>
  <div class="sprint-container">
    <el-card class="selector-card" shadow="hover">
      <div style="display: flex; gap: 10px; align-items: center;">
        <el-select v-model="selectedProjectId" placeholder="请选择项目" @change="handleProjectChange" style="width: 200px;">
          <el-option v-for="project in projectStore.projects" :key="project.id" :label="project.name" :value="project.id" />
        </el-select>
        <el-button type="primary" @click="openSprintDialog()">创建 Sprint</el-button>
      </div>
    </el-card>

    <el-card class="sprint-card" shadow="hover">
      <template #header><span>Sprint 列表</span></template>
      <el-table :data="sprintStore.sprints" v-loading="sprintStore.isLoading" stripe>
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="createdTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openSprintDialog(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="sprintStore.total"
        :page-sizes="[5, 10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 创建/编辑 Sprint 对话框 -->
    <el-dialog :title="editingSprint ? '编辑 Sprint' : '创建 Sprint'" v-model="dialogVisible" width="500px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="名称" required><el-input v-model="formData.name" placeholder="请输入 Sprint 名称" /></el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="formData.startDate" type="date" placeholder="请选择开始日期" value-format="YYYY-MM-DD" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="formData.endDate" type="date" placeholder="请选择结束日期" value-format="YYYY-MM-DD" style="width: 100%;" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSprint" :loading="sprintStore.isLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useProjectStore } from '@/stores/project'
import { useSprintStore } from '@/stores/sprint'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { SprintVO, SprintCreateDTO } from '@/types/sprint'

const projectStore = useProjectStore()
const sprintStore = useSprintStore()

const selectedProjectId = ref<string | number | null>(null)
const dialogVisible = ref(false)
const editingSprint = ref<SprintVO | null>(null)
const currentPage = ref(1)
const pageSize = ref(10)

const formData = ref<SprintCreateDTO>({
  name: '',
  startDate: undefined,
  endDate: undefined,
  projectId: null as any
})

const handleProjectChange = async () => {
  if (selectedProjectId.value) {
    formData.value.projectId = selectedProjectId.value
    currentPage.value = 1
    await loadSprints()
  }
}

const loadSprints = async () => {
  await sprintStore.fetchSprintsPage(selectedProjectId.value as string | number, {
    page: currentPage.value - 1,
    size: pageSize.value
  })
}

const handleSizeChange = () => {
  currentPage.value = 1
  loadSprints()
}
const handleCurrentChange = () => loadSprints()

const openSprintDialog = (sprint?: SprintVO) => {
  if (sprint) {
    editingSprint.value = sprint
    formData.value = {
      name: sprint.name,
      startDate: sprint.startDate,
      endDate: sprint.endDate,
      projectId: sprint.projectId
    }
  } else {
    editingSprint.value = null
    formData.value = {
      name: '',
      startDate: undefined,
      endDate: undefined,
      projectId: selectedProjectId.value as string | number
    }
  }
  dialogVisible.value = true
}

const saveSprint = async () => {
  if (!formData.value.name) {
    ElMessage.error('Sprint 名称不能为空')
    return
  }
  if (!formData.value.projectId) {
    ElMessage.error('请先选择项目')
    return
  }
  try {
    if (editingSprint.value) {
      await sprintStore.updateSprint(editingSprint.value.id, {
        name: formData.value.name,
        startDate: formData.value.startDate,
        endDate: formData.value.endDate
      })
    } else {
      await sprintStore.createSprint(formData.value)
    }
    dialogVisible.value = false
    await loadSprints()
  } catch (error) {}
}

const handleDelete = async (id: string | number) => {
  await ElMessageBox.confirm('确定删除该 Sprint 吗？', '警告', { type: 'warning' })
  await sprintStore.deleteSprint(id)
  await loadSprints()
}

onMounted(async () => {
  await projectStore.fetchProjects()
  if (projectStore.projects.length) {
    selectedProjectId.value = projectStore.projects[0].id
    formData.value.projectId = selectedProjectId.value
    await loadSprints()
  }
})
</script>

<style scoped lang="scss">
.sprint-container {
  .selector-card { margin-bottom: 20px; }
  .sprint-card .card-header { display: flex; justify-content: space-between; align-items: center; }
}
</style>