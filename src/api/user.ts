import request from './request'
import type { UserUpdateDTO, PasswordChangeDTO, UserVO } from '@/types/user'

export const userApi = {
  getCurrentUser() {
    return request.get('/users/me') as Promise<UserVO>
  },
  updateProfile(data: UserUpdateDTO) {
    return request.put('/users/me', data) as Promise<UserVO>
  },
  changePassword(data: PasswordChangeDTO) {
    return request.put('/users/me/password', data) as Promise<void>
  },
  updateAvatar(avatarUrl: string) {
    return request.put('/users/me/avatar', null, { params: { avatarUrl } }) as Promise<UserVO>
  }
}