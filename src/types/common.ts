export interface ApiResponse<T = any> {
  code: string
  message: string
  data: T
  timestamp: string
}

export interface PageParams {
  page?: number
  size?: number
  sort?: string
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}