export interface BugCreateDTO {
  title: string
  severity?: string
  projectId: number | string
}

export interface BugUpdateDTO {
  title: string
  severity?: string
}

export interface BugVO {
  id: number | string
  title: string
  severity?: string
  projectId: number | string
  createdTime: string
  updatedTime: string
}