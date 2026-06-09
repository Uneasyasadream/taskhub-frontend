import axios, { 
  type AxiosInstance, 
  type AxiosError, 
  type AxiosResponse,        // 添加这一行
  type InternalAxiosRequestConfig 
} from 'axios'               // 去掉多余的单引号
import type { ApiResponse } from '../types/common'
import { ElMessage } from 'element-plus'

const request: AxiosInstance = axios.create({
  baseURL: 'api/v1',
  timeout: 10000
})

// 请求拦截器：自动携带 Token
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {   // 建议加上类型注解
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers = config.headers || {}
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const { code, message, data } = response.data
    if (code === '00000') {
      return data as any
    }
    if (code === 'A0401') {
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      window.location.href = '/login'
      ElMessage.error('登录已过期，请重新登录')
      return Promise.reject(new Error('Unauthorized'))
    }
    ElMessage.error(message || '操作失败')
    return Promise.reject(new Error(message))
  },
  (error: AxiosError) => {
    const status = error.response?.status
    if (status === 401) {
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      window.location.href = '/login'
      ElMessage.error('登录已过期，请重新登录')
    } else if (status === 403) {
      ElMessage.error('权限不足')
    } else if (status === 500) {
      ElMessage.error('服务器错误')
    } else if (status === 404) {
      ElMessage.error('请求资源不存在')
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请重试')
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request