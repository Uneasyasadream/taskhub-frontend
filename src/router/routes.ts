import type { RouteRecordRaw } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'
import EmptyLayout from '@/layouts/EmptyLayout.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    component: EmptyLayout,
    children: [
      {
        path: '',
        name: 'Login',
        component: () => import('@/views/login/LoginView.vue'),
        meta: { title: '登录', requiresAuth: false }
      }
    ]
  },
  {
    path: '/register',
    component: EmptyLayout,
    children: [
      {
        path: '',
        name: 'Register',
        component: () => import('@/views/login/RegisterView.vue'),
        meta: { title: '注册', requiresAuth: false }
      }
    ]
  },
  {
    path: '/',
    component: MainLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/DashboardView.vue'),
        meta: { title: '仪表板', requiresAuth: true }
      },
      {
        path: 'projects',
        name: 'Projects',
        component: () => import('@/views/project/ProjectView.vue'),
        meta: { title: '项目列表', requiresAuth: true }
      },
      {
        path: 'projects/:id/members',
        name: 'ProjectMembers',
        component: () => import('@/views/project/projectMemberView.vue'),
        meta: { title: '项目成员', requiresAuth: true }
      },
      {
        path: 'kanban',
        name: 'Kanban',
        component: () => import('@/views/task/TaskView.vue'),
        meta: { title: '任务看板', requiresAuth: true }
      },
      {
        path: 'bugs',
        name: 'Bugs',
        component: () => import('@/views/bug/BugView.vue'),
        meta: { title: '缺陷管理', requiresAuth: true }
      },
      {
        path: 'sprints',
        name: 'Sprints',
        component: () => import('@/views/sprint/SprintView.vue'),
        meta: { title: '迭代管理', requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/user/ProfileView.vue'),
        meta: { title: '个人资料', requiresAuth: true }
      },
      {
        path: 'ai-assistant',
        name: 'AIAssistant',
        component: () => import('@/views/ai/AIAssistantView.vue'),
        meta: { title: 'AI 助手', requiresAuth: true }
}
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard'
  }
]

export default routes