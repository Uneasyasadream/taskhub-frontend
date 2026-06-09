import { defineStore } from 'pinia'
import { ref } from 'vue'
import { authApi } from '@/api/auth'
import { userApi } from '@/api/user'
import type { LoginRequest, RegisterRequest, CurrentUser } from '@/types/auth'
import type { UserUpdateDTO, PasswordChangeDTO } from '@/types/user'

export const useAuthStore = defineStore('auth', () => {
  const accessToken = ref<string | null>(localStorage.getItem('accessToken') || null)
  const refreshToken = ref<string | null>(localStorage.getItem('refreshToken') || null)
  const currentUser = ref<CurrentUser | null>(null)
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  const getAccessToken = () => accessToken.value

  async function login(data: LoginRequest) {
    try {
      isLoading.value = true
      error.value = null
      const tokenData = await authApi.login(data)
      accessToken.value = tokenData.accessToken
      refreshToken.value = tokenData.refreshToken
      localStorage.setItem('accessToken', tokenData.accessToken)
      localStorage.setItem('refreshToken', tokenData.refreshToken)
      
      const userInfo = await userApi.getCurrentUser()
      currentUser.value = userInfo
      return true
    } catch (err: any) {
      error.value = err.message || '登录失败'
      return false
    } finally {
      isLoading.value = false
    }
  }

  async function register(data: RegisterRequest) {
    try {
      isLoading.value = true
      error.value = null
      await authApi.register(data)
      return true
    } catch (err: any) {
      error.value = err.message || '注册失败'
      return false
    } finally {
      isLoading.value = false
    }
  }

  async function updateProfile(data: UserUpdateDTO) {
    try {
      isLoading.value = true
      const updated = await userApi.updateProfile(data)
      currentUser.value = { ...currentUser.value, ...updated }
      return updated
    } catch (err: any) {
      error.value = err.message || '更新资料失败'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function changePassword(data: PasswordChangeDTO) {
    try {
      isLoading.value = true
      await userApi.changePassword(data)
    } catch (err: any) {
      error.value = err.message || '修改密码失败'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function updateAvatar(avatarUrl: string) {
    try {
      isLoading.value = true
      const updated = await userApi.updateAvatar(avatarUrl)
      currentUser.value = { ...currentUser.value, ...updated }
      return updated
    } catch (err: any) {
      error.value = err.message || '更新头像失败'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  function logout() {
    accessToken.value = null
    refreshToken.value = null
    currentUser.value = null
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
  }

  return {
    accessToken,
    refreshToken,
    currentUser,
    isLoading,
    error,
    getAccessToken,
    login,
    register,
    updateProfile,
    changePassword,
    updateAvatar,
    logout
  }
})