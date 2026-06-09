import request from './request'
import type { ProjectCreateDTO, ProjectUpdateDTO, ProjectVO } from '@/types/project'
import type { PageParams, PageResponse } from '@/types/common'

export const projectApi = {
  createProject(data: ProjectCreateDTO) {
    return request.post('/projects', data) as Promise<ProjectVO>
  },
  getProjectList(params?: PageParams) {
    return request.get('/projects', { params }) as Promise<ProjectVO[]>
  },
  getProjectById(id: string | number) {
    return request.get(`/projects/${id}`) as Promise<ProjectVO>
  },
  updateProject(id: string | number, data: ProjectUpdateDTO) {
    return request.put(`/projects/${id}`, data) as Promise<ProjectVO>
  },
  deleteProject(id: string | number) {
    return request.delete(`/projects/${id}`) as Promise<void>
  },
  getProjectListPage(params?: PageParams) {
    return request.get('/projects/page', { params }) as Promise<PageResponse<ProjectVO>>
  }
}