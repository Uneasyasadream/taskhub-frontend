<template>
  <div class="bug-container">
    <el-card class="selector-card" shadow="hover">
      <div style="display: flex; gap: 10px; align-items: center;">
        <el-select v-model="selectedProjectId" placeholder="请选择项目" @change="handleProjectChange" style="width: 200px;">
          <el-option v-for="project in projectStore.projects" :key="project.id" :label="project.name" :value="project.id" />
        </el-select>
        <el-button type="primary" @click="openBugDialog()">报告缺陷</el-button>
      </div>
    </el-card>

    <el-card class="bug-card" shadow="hover">
      <template #header><span>缺陷列表</span></template>
      <el-table :data="bugStore.bugs" v-loading="bugStore.isLoading" stripe>
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="severity" label="严重程度" width="100">
          <template #default="{ row }">
            <el-tag :type="getSeverityType(row.severity)">{{ row.severity || 'P2' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="报告时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openBugDialog(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="bugStore.total"
        :page-sizes="[5, 10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 编辑/创建对话框 -->
    <el-dialog :title="editingBug ? '编辑缺陷' : '报告缺陷'" v-model="dialogVisible" width="500px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="标题" required><el-input v-model="formData.title" /></el-form-item>
        <el-form-item label="严重程度">
          <el-select v-model="formData.severity">
            <el-option label="P0 (紧急)" value="P0" /><el-option label="P1 (高)" value="P1" />
            <el-option label="P2 (中)" value="P2" /><el-option label="P3 (低)" value="P3" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveBug">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useProjectStore } from '@/stores/project'
import { useBugStore } from '@/stores/bug'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { BugVO, BugCreateDTO } from '@/types/bug'

const projectStore = useProjectStore()
const bugStore = useBugStore()
const selectedProjectId = ref<string | number | null>(null)
const dialogVisible = ref(false)
const editingBug = ref<BugVO | null>(null)
const currentPage = ref(1)
const pageSize = ref(10)

const formData = ref<BugCreateDTO>({ title: '', severity: 'P2', projectId: null as any })

const getSeverityType = (severity?: string) => {
  switch (severity) {
    case 'P0': return 'danger'
    case 'P1': return 'warning'
    default: return 'info'
  }
}

const handleProjectChange = async () => {
  if (selectedProjectId.value) {
    formData.value.projectId = selectedProjectId.value
    currentPage.value = 1
    await loadBugs()
  }
}

const loadBugs = async () => {
  await bugStore.fetchBugsPage(selectedProjectId.value as string | number, {
    page: currentPage.value - 1,
    size: pageSize.value
  })
}

const handleSizeChange = () => {
  currentPage.value = 1
  loadBugs()
}
const handleCurrentChange = () => loadBugs()

const openBugDialog = (bug?: BugVO) => {
  if (bug) {
    editingBug.value = bug
    formData.value = { title: bug.title, severity: bug.severity || 'P2', projectId: bug.projectId }
  } else {
    editingBug.value = null
    formData.value = { title: '', severity: 'P2', projectId: selectedProjectId.value as string | number }
  }
  dialogVisible.value = true
}

const saveBug = async () => {
  if (!formData.value.title) return ElMessage.error('标题不能为空')
  if (!formData.value.projectId) return ElMessage.error('请先选择项目')
  try {
    if (editingBug.value) {
      await bugStore.updateBug(editingBug.value.id, { title: formData.value.title, severity: formData.value.severity })
    } else {
      await bugStore.createBug(formData.value)
    }
    dialogVisible.value = false
    await loadBugs()
  } catch (error) {}
}

const handleDelete = async (id: string | number) => {
  await ElMessageBox.confirm('确定删除该缺陷吗？', '警告', { type: 'warning' })
  await bugStore.deleteBug(id)
  await loadBugs()
}

onMounted(async () => {
  await projectStore.fetchProjects()
  if (projectStore.projects.length) {
    selectedProjectId.value = projectStore.projects[0].id
    formData.value.projectId = selectedProjectId.value
    await loadBugs()
  }
})
</script>

<style scoped lang="scss">
.bug-container {
  .selector-card { margin-bottom: 20px; }
  .bug-card .card-header { display: flex; justify-content: space-between; align-items: center; }
}
</style>