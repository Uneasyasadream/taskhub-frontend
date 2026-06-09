import request from './request'
import type { SprintCreateDTO, SprintUpdateDTO, SprintVO } from '@/types/sprint'
import type { PageParams, PageResponse } from '@/types/common'

export const sprintApi = {
  createSprint(data: SprintCreateDTO) {
    return request.post('/sprints', data) as Promise<SprintVO>
  },
  getSprintList(projectId: string | number, params?: PageParams) {
    return request.get('/sprints', { params: { projectId, ...params } }) as Promise<SprintVO[]>
  },
  getSprintListPage(projectId: string | number, params: PageParams) {
    return request.get('/sprints/page', { params: { projectId, ...params } }) as Promise<PageResponse<SprintVO>>
  },
  getSprintById(id: string | number) {
    return request.get(`/sprints/${id}`) as Promise<SprintVO>
  },
  updateSprint(id: string | number, data: SprintUpdateDTO) {
    return request.put(`/sprints/${id}`, data) as Promise<SprintVO>
  },
  deleteSprint(id: string | number) {
    return request.delete(`/sprints/${id}`) as Promise<void>
  }
}