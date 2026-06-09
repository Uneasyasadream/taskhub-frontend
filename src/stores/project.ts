import { defineStore } from 'pinia'
import { ref } from 'vue'
import { projectApi } from '@/api/project'
import type { ProjectVO, ProjectCreateDTO, ProjectUpdateDTO } from '@/types/project'
import { ElMessage } from 'element-plus'

export const useProjectStore = defineStore('project', () => {
  const projects = ref<ProjectVO[]>([])
  const isLoading = ref(false)
  const error = ref('')

  async function fetchProjects() {
    try {
      isLoading.value = true
      const data = await projectApi.getProjectList()
      projects.value = (Array.isArray(data) ? data : []).map(item => ({
        ...item,
        id: String(item.id),
        ownerId: String(item.ownerId)
      }))
      error.value = ''
    } catch (err: any) {
      error.value = err?.message || '获取项目列表失败'
      if (error.value) ElMessage.error(error.value)
    } finally {
      isLoading.value = false
    }
  }

  async function createProject(data: ProjectCreateDTO) {
    try {
      isLoading.value = true
      const newProject = await projectApi.createProject(data)
      await fetchProjects()
      ElMessage.success('项目创建成功')
      return newProject
    } catch (err: any) {
      error.value = err?.message || '创建项目失败'
      if (error.value) ElMessage.error(error.value)
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function updateProject(id: string | number, data: ProjectUpdateDTO) {
    try {
      isLoading.value = true
      const idStr = String(id)
      const updated = await projectApi.updateProject(idStr, data)
      const index = projects.value.findIndex(p => String(p.id) === idStr)
      if (index !== -1) projects.value[index] = updated
      ElMessage.success('项目更新成功')
      return updated
    } catch (err: any) {
      error.value = err?.message || '更新项目失败'
      if (error.value) ElMessage.error(error.value)
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function deleteProject(id: string | number) {
    if (!id) {
      ElMessage.error('无效的项目ID')
      return
    }
    const idStr = String(id)
    try {
      isLoading.value = true
      await projectApi.deleteProject(idStr)
      projects.value = projects.value.filter(p => String(p.id) !== idStr)
      ElMessage.success('项目删除成功')
    } catch (err: any) {
      error.value = err?.message || '删除项目失败'
      if (error.value) ElMessage.error(error.value)
      throw err
    } finally {
      isLoading.value = false
    }
  }

  function clearError() {
    error.value = ''
  }

  return {
    projects,
    isLoading,
    error,
    fetchProjects,
    createProject,
    updateProject,
    deleteProject,
    clearError
  }
})