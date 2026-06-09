import request from './request'
import type { LoginRequest, RegisterRequest, TokenResponse } from '@/types/auth'

export const authApi = {
  login(data: LoginRequest) {
    return request.post('/auth/login', data) as Promise<TokenResponse>
  },
  register(data: RegisterRequest) {
    return request.post('/auth/register', data) as Promise<boolean>
  },
  refreshToken(refreshToken: string) {
    return request.post('/auth/refresh', { refreshToken }) as Promise<TokenResponse>
  },
  logout() {
    return request.post('/auth/logout') as Promise<void>
  }
}