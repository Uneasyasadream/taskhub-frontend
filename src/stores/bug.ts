import { defineStore } from 'pinia'
import { ref } from 'vue'
import { bugApi } from '@/api/bug'
import type { BugVO, BugCreateDTO, BugUpdateDTO } from '@/types/bug'
import type { PageParams } from '@/types/common'

export const useBugStore = defineStore('bug', () => {
  const bugs = ref<BugVO[]>([])
  const total = ref(0)
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  async function fetchBugs(projectId: number | string) {
    try {
      isLoading.value = true
      const data = await bugApi.getBugList(projectId)
      bugs.value = Array.isArray(data) ? data : []
      total.value = bugs.value.length
    } catch (err: any) {
      error.value = err.message || '获取缺陷列表失败'
    } finally {
      isLoading.value = false
    }
  }

  async function fetchBugsPage(projectId: number | string, params: PageParams) {
    try {
      isLoading.value = true
      const pageData = await bugApi.getBugListPage(projectId, params)
      bugs.value = pageData.content
      total.value = pageData.totalElements
    } catch (err: any) {
      error.value = err.message || '获取缺陷列表失败'
    } finally {
      isLoading.value = false
    }
  }

  async function createBug(data: BugCreateDTO) {
    try {
      isLoading.value = true
      const newBug = await bugApi.createBug(data)
      bugs.value.unshift(newBug)
      total.value++
      return newBug
    } catch (err: any) {
      error.value = err.message || '创建缺陷失败'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function updateBug(id: number | string, data: BugUpdateDTO) {
    try {
      isLoading.value = true
      const updated = await bugApi.updateBug(id, data)
      const index = bugs.value.findIndex(b => b.id === id)
      if (index !== -1) bugs.value[index] = updated
      return updated
    } catch (err: any) {
      error.value = err.message || '更新缺陷失败'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function deleteBug(id: number | string) {
    try {
      isLoading.value = true
      await bugApi.deleteBug(id)
      bugs.value = bugs.value.filter(b => b.id !== id)
      total.value--
    } catch (err: any) {
      error.value = err.message || '删除缺陷失败'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  return {
    bugs,
    total,
    isLoading,
    error,
    fetchBugs,
    fetchBugsPage,
    createBug,
    updateBug,
    deleteBug
  }
})