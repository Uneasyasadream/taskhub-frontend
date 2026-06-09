import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { taskApi } from '@/api/task'
import type { TaskVO, TaskCreateDTO, TaskUpdateDTO } from '@/types/task'
import { TASK_STATUS } from '@/types/task'
import { ElMessage } from 'element-plus'

export const useTaskStore = defineStore('task', () => {
  const tasks = ref<TaskVO[]>([])
  const isLoading = ref(false)

  const todoTasks = computed(() => tasks.value.filter(t => t.status === TASK_STATUS.TODO))
  const inProgressTasks = computed(() => tasks.value.filter(t => t.status === TASK_STATUS.IN_PROGRESS))
  const completedTasks = computed(() => tasks.value.filter(t => t.status === TASK_STATUS.COMPLETED))

  async function fetchTasks(projectId: string | number) {
    if (!projectId) return
    try {
      isLoading.value = true
      const pageData = await taskApi.getTaskList(projectId)
      tasks.value = (pageData.content || []).map(t => ({
        ...t,
        id: String(t.id),
        projectId: String(t.projectId),
        assigneeId: t.assigneeId ? String(t.assigneeId) : undefined
      }))
    } catch (err: any) {
      ElMessage.error(err.message || '获取任务列表失败')
    } finally {
      isLoading.value = false
    }
  }

  async function createTask(data: TaskCreateDTO) {
    try {
      isLoading.value = true
      const newTask = await taskApi.createTask(data)
      tasks.value.unshift({
        ...newTask,
        id: String(newTask.id),
        projectId: String(newTask.projectId),
        assigneeId: newTask.assigneeId ? String(newTask.assigneeId) : undefined
      })
      ElMessage.success('创建成功')
      if (data.projectId) await fetchTasks(data.projectId)
    } catch (err: any) {
      ElMessage.error(err.message || '创建失败')
    } finally {
      isLoading.value = false
    }
  }

  async function updateTask(id: string | number, data: TaskUpdateDTO) {
    const idStr = String(id)
    try {
      isLoading.value = true
      const updated = await taskApi.updateTask(idStr, data)
      const index = tasks.value.findIndex(t => t.id === idStr)
      if (index !== -1) {
        tasks.value[index] = {
          ...updated,
          id: String(updated.id),
          projectId: String(updated.projectId),
          assigneeId: updated.assigneeId ? String(updated.assigneeId) : undefined
        }
      }
      ElMessage.success('更新成功')
    } catch (err: any) {
      ElMessage.error(err.message || '更新失败')
    } finally {
      isLoading.value = false
    }
  }

  async function deleteTask(id: string | number) {
    const idStr = String(id)
    try {
      isLoading.value = true
      await taskApi.deleteTask(idStr)
      tasks.value = tasks.value.filter(t => t.id !== idStr)
      ElMessage.success('删除成功')
    } catch (err: any) {
      ElMessage.error(err.message || '删除失败')
    } finally {
      isLoading.value = false
    }
  }

  return {
    tasks,
    isLoading,
    todoTasks,
    inProgressTasks,
    completedTasks,
    fetchTasks,
    createTask,
    updateTask,
    deleteTask
  }
})