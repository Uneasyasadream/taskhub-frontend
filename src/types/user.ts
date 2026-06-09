export interface CurrentUser {
  id: number | string
  username: string
  nickname?: string
  email?: string
  phone?: string
  avatar?: string
  status?: number
  isAdmin?: number
}

export interface UserVO {
  id: number | string
  username: string
  nickname?: string
  email?: string
  phone?: string
  avatar?: string
  status?: number
  isAdmin?: number
  createdTime: string
  updatedTime: string
}

export interface UserUpdateDTO {
  nickname?: string
  email?: string
  phone?: string
  avatar?: string
}

export interface PasswordChangeDTO {
  oldPassword: string
  newPassword: string
}