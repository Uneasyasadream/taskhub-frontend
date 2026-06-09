<template>
  <div>
    <el-card>
      <div style="display: flex; gap: 10px; margin-bottom: 20px;">
        <el-select v-model="selectedProjectId" placeholder="请选择项目" @change="onProjectChange" style="width: 200px;">
          <el-option v-for="p in projectStore.projects" :key="p.id" :label="p.name" :value="p.id" />
        </el-select>
        <el-button type="primary" @click="openTaskDialog()">创建任务</el-button>
      </div>

      <div class="kanban-board">
        <div v-for="status in statuses" :key="status.value" class="kanban-column">
          <div class="column-header">
            <h3>{{ status.label }}</h3>
            <span class="count">{{ getTasksByStatus(status.value).length }}</span>
          </div>
          <div class="tasks-list">
            <div v-for="task in getTasksByStatus(status.value)" :key="task.id" class="task-card">
              <div class="task-title">{{ task.title }}</div>
              <div class="task-desc">{{ task.description }}</div>
              <div class="task-footer">
                <el-button link size="small" @click="openTaskDialog(task)">编辑</el-button>
                <el-button link size="small" type="danger" @click="handleDelete(task)">删除</el-button>
                <el-button v-if="task.status === 1" link size="small" @click="moveTask(task, 0)">← 待办</el-button>
                <el-button v-if="task.status === 2" link size="small" @click="moveTask(task, 1)">← 进行中</el-button>
                <el-button v-if="task.status !== 2 && task.status !== 1" link size="small" type="success" @click="moveTask(task, 1)">→ 进行中</el-button>
                <el-button v-if="task.status === 1" link size="small" type="success" @click="moveTask(task, 2)">→ 完成</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 创建/编辑任务对话框 -->
    <el-dialog :title="editingTask ? '编辑任务' : '创建任务'" v-model="dialogVisible" width="500px">
      <el-form :model="taskForm" label-width="80px">
        <el-form-item label="标题" required>
          <el-input v-model="taskForm.title" placeholder="请输入任务标题" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="taskForm.description" type="textarea" placeholder="请输入任务描述" />
        </el-form-item>
        <el-form-item label="项目">
          <el-select v-model="taskForm.projectId" disabled>
            <el-option :label="selectedProjectLabel" :value="selectedProjectId" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人">
          <el-select v-model="taskForm.assigneeId" clearable placeholder="请选择负责人">
            <el-option v-for="m in projectMembers" :key="m.userId" :label="m.nickname || m.username" :value="Number(m.userId)" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveTask" :loading="taskStore.isLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useProjectStore } from '@/stores/project'
import { useTaskStore } from '@/stores/task'
import { projectMemberApi } from '@/api/projectMember'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { TaskVO, TaskCreateDTO } from '@/types/task'

const projectStore = useProjectStore()
const taskStore = useTaskStore()
const projectMembers = ref<any[]>([])

const selectedProjectId = ref<string | number | null>(null)
const selectedProjectLabel = computed(() => {
  const p = projectStore.projects.find(p => String(p.id) === String(selectedProjectId.value))
  return p?.name || ''
})
const dialogVisible = ref(false)
const editingTask = ref<TaskVO | null>(null)
const taskForm = ref<TaskCreateDTO>({
  title: '',
  description: '',
  projectId: 0,
  status: 0,
  assigneeId: undefined
})

const statuses = [
  { value: 0, label: '待办' },
  { value: 1, label: '进行中' },
  { value: 2, label: '已完成' }
]

const getTasksByStatus = (status: number) => {
  return taskStore.tasks.filter(t => t.status === status)
}

const loadProjectMembers = async () => {
  if (selectedProjectId.value) {
    const members = await projectMemberApi.getMembers(String(selectedProjectId.value))
    projectMembers.value = members
  }
}

const onProjectChange = async () => {
  if (selectedProjectId.value) {
    await taskStore.fetchTasks(selectedProjectId.value)
    await loadProjectMembers()
  } else {
    taskStore.tasks = []
  }
}

const openTaskDialog = (task?: TaskVO) => {
  if (task) {
    editingTask.value = task
    taskForm.value = {
      title: task.title,
      description: task.description || '',
      projectId: task.projectId,
      status: task.status,
      assigneeId: task.assigneeId ? Number(task.assigneeId) : undefined
    }
  } else {
    editingTask.value = null
    taskForm.value = {
      title: '',
      description: '',
      projectId: selectedProjectId.value as number,
      status: 0,
      assigneeId: undefined
    }
  }
  dialogVisible.value = true
}

const saveTask = async () => {
  if (!taskForm.value.title) {
    ElMessage.error('标题不能为空')
    return
  }
  if (!taskForm.value.projectId) {
    ElMessage.error('请先选择项目')
    return
  }

  try {
    if (editingTask.value) {
      await taskStore.updateTask(editingTask.value.id, taskForm.value)
    } else {
      await taskStore.createTask(taskForm.value)
    }
    dialogVisible.value = false
    if (selectedProjectId.value) {
      await taskStore.fetchTasks(selectedProjectId.value)
    }
  } catch (error) {
    // 已处理
  }
}

const moveTask = async (task: TaskVO, newStatus: number) => {
  try {
    await taskStore.updateTask(task.id, { ...task, status: newStatus })
  } catch (error) {}
}

const handleDelete = async (task: TaskVO) => {
  try {
    await ElMessageBox.confirm(`确定删除任务“${task.title}”吗？`, '提示', { type: 'warning' })
    await taskStore.deleteTask(task.id)
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

onMounted(async () => {
  await projectStore.fetchProjects()
  if (projectStore.projects.length > 0) {
    selectedProjectId.value = projectStore.projects[0].id
    await taskStore.fetchTasks(selectedProjectId.value)
    await loadProjectMembers()
  }
})

watch(selectedProjectId, (newVal, oldVal) => {
  if (newVal && newVal !== oldVal) {
    taskStore.fetchTasks(newVal)
    loadProjectMembers()
  }
})
</script>

<style scoped>
.kanban-board {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}
.kanban-column {
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 12px;
}
.column-header {
  display: flex;
  justify-content: space-between;
  border-bottom: 2px solid #dcdfe6;
  margin-bottom: 10px;
  padding-bottom: 8px;
}
.tasks-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  min-height: 200px;
}
.task-card {
  background-color: white;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 12px;
}
.task-title {
  font-weight: bold;
}
.task-desc {
  font-size: 12px;
  color: #909399;
  margin: 5px 0;
}
.task-footer {
  display: flex;
  gap: 8px;
  margin-top: 8px;
  flex-wrap: wrap;
}
</style>