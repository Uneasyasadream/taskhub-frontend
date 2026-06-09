export interface SprintCreateDTO {
  name: string
  startDate?: string
  endDate?: string
  projectId: number | string
}

export interface SprintUpdateDTO {
  name: string
  startDate?: string
  endDate?: string
}

export interface SprintVO {
  id: number | string
  name: string
  startDate?: string
  endDate?: string
  projectId: number | string
  createdTime: string
  updatedTime: string
}