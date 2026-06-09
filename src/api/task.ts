import request from './request'
import type { TaskCreateDTO, TaskUpdateDTO, TaskVO } from '@/types/task'
import type { PageResponse } from '@/types/common'

export const taskApi = {
  createTask(data: TaskCreateDTO) {
    return request.post('/tasks', data) as Promise<TaskVO>
  },
  getTaskList(projectId: string | number) {
    if (!projectId) throw new Error('projectId is required')
    return request.get('/tasks', { params: { projectId } }) as Promise<PageResponse<TaskVO>>
  },
  getTaskById(id: string | number) {
    return request.get(`/tasks/${id}`) as Promise<TaskVO>
  },
  updateTask(id: string | number, data: TaskUpdateDTO) {
    return request.put(`/tasks/${id}`, data) as Promise<TaskVO>
  },
  deleteTask(id: string | number) {
    return request.delete(`/tasks/${id}`) as Promise<void>
  }
}