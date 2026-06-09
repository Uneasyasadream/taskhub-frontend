import { createRouter, createWebHistory } from 'vue-router'
import routes from './routes'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const authStore = useAuthStore()
  const token = authStore.getAccessToken()
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)

  document.title = (to.meta.title as string) || 'TaskHub'

  if (requiresAuth && !token) {
    return '/login'
  }
  if (token && to.path === '/login') {
    return '/dashboard'
  }
  return true
})

export default router