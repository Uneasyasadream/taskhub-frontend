import request from './request'
import type { BugCreateDTO, BugUpdateDTO, BugVO } from '@/types/bug'
import type { PageParams, PageResponse } from '@/types/common'

export const bugApi = {
  createBug(data: BugCreateDTO) {
    return request.post('/bugs', data) as Promise<BugVO>
  },
  getBugList(projectId: string | number, params?: PageParams) {
    return request.get('/bugs', { params: { projectId, ...params } }) as Promise<BugVO[]>
  },
  getBugListPage(projectId: string | number, params: PageParams) {
    return request.get('/bugs/page', { params: { projectId, ...params } }) as Promise<PageResponse<BugVO>>
  },
  getBugById(id: string | number) {
    return request.get(`/bugs/${id}`) as Promise<BugVO>
  },
  updateBug(id: string | number, data: BugUpdateDTO) {
    return request.put(`/bugs/${id}`, data) as Promise<BugVO>
  },
  deleteBug(id: string | number) {
    return request.delete(`/bugs/${id}`) as Promise<void>
  }
}