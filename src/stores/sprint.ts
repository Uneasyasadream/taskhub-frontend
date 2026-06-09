import { defineStore } from 'pinia'
import { ref } from 'vue'
import { sprintApi } from '@/api/sprint'
import type { SprintVO, SprintCreateDTO, SprintUpdateDTO } from '@/types/sprint'
import type { PageParams } from '@/types/common'

export const useSprintStore = defineStore('sprint', () => {
  const sprints = ref<SprintVO[]>([])
  const total = ref(0)
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  async function fetchSprints(projectId: number | string) {
    try {
      isLoading.value = true
      const data = await sprintApi.getSprintList(projectId)
      sprints.value = Array.isArray(data) ? data : []
      total.value = sprints.value.length
    } catch (err: any) {
      error.value = err.message || '获取迭代列表失败'
    } finally {
      isLoading.value = false
    }
  }

  async function fetchSprintsPage(projectId: number | string, params: PageParams) {
    try {
      isLoading.value = true
      const pageData = await sprintApi.getSprintListPage(projectId, params)
      sprints.value = pageData.content
      total.value = pageData.totalElements
    } catch (err: any) {
      error.value = err.message || '获取迭代列表失败'
    } finally {
      isLoading.value = false
    }
  }

  async function createSprint(data: SprintCreateDTO) {
    try {
      isLoading.value = true
      const newSprint = await sprintApi.createSprint(data)
      sprints.value.push(newSprint)
      total.value++
      return newSprint
    } catch (err: any) {
      error.value = err.message || '创建迭代失败'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function updateSprint(id: number | string, data: SprintUpdateDTO) {
    try {
      isLoading.value = true
      const updated = await sprintApi.updateSprint(id, data)
      const index = sprints.value.findIndex(s => s.id === id)
      if (index !== -1) sprints.value[index] = updated
      return updated
    } catch (err: any) {
      error.value = err.message || '更新迭代失败'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function deleteSprint(id: number | string) {
    try {
      isLoading.value = true
      await sprintApi.deleteSprint(id)
      sprints.value = sprints.value.filter(s => s.id !== id)
      total.value--
    } catch (err: any) {
      error.value = err.message || '删除迭代失败'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  return {
    sprints,
    total,
    isLoading,
    error,
    fetchSprints,
    fetchSprintsPage,
    createSprint,
    updateSprint,
    deleteSprint
  }
})