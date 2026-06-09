export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  nickname?: string
  phone?: string
  email?: string
  inviteCode?: string   // 新增
}

export interface TokenResponse {
  accessToken: string
  refreshToken: string
  tokenType: string
  expiresIn: number
}

export interface CurrentUser {
  id: number | string
  username: string
  email?: string
  phone?: string
  nickname?: string
  avatar?: string
  status?: number
  isAdmin?: number
}