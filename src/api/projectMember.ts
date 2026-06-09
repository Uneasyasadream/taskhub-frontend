import request from './request'

export const projectMemberApi = {
  getMembers(projectId: string | number) {
    return request.get(`/projects/${projectId}/members`) as Promise<any[]>
  },
  addMember(projectId: string | number, data: { userId: string | number; role: string }) {
    // 后端 DTO 中 userId 是 Long，Spring 可自动转换字符串
    return request.post(`/projects/${projectId}/members`, data) as Promise<any>
  },
  updateRole(projectId: string | number, userId: number | string, data: { role: string }) {
    return request.put(`/projects/${projectId}/members/${userId}`, data) as Promise<any>
  },
  removeMember(projectId: string | number, userId: number | string) {
    return request.delete(`/projects/${projectId}/members/${userId}`) as Promise<void>
  }
}