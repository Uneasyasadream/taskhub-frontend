export const TASK_STATUS = {
  TODO: 0,
  IN_PROGRESS: 1,
  COMPLETED: 2
} as const

export const TASK_STATUS_LABELS: Record<number, string> = {
  0: '待办',
  1: '进行中',
  2: '已完成'
}

export interface TaskCreateDTO {
  title: string
  description?: string
  status?: number
  projectId: number | string
  assigneeId?: number | string
}

export interface TaskUpdateDTO {
  title: string
  description?: string
  status?: number
  assigneeId?: number | string
}

export interface TaskVO {
  id: number | string
  title: string
  description?: string
  status: number
  projectId: number | string
  assigneeId?: number | string
  createdTime: string
  updatedTime: string
}