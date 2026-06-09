export interface ProjectCreateDTO {
  name: string
  description?: string
}

export interface ProjectUpdateDTO {
  name: string
  description?: string
}

export interface ProjectVO {
  id: number | string
  name: string
  description?: string
  ownerId: number | string
  createdTime: string
  updatedTime: string
}