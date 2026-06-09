<template>
  <el-container class="main-layout">
    <el-aside class="main-aside" width="220px">
      <div class="logo">
        <div class="logo-icon">TH</div>
        <span>TaskHub</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="menu"
        router
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>仪表板</span>
        </el-menu-item>
        <el-menu-item index="/projects">
          <el-icon><Folder /></el-icon>
          <span>项目</span>
        </el-menu-item>
        <el-menu-item index="/kanban">
          <el-icon><List /></el-icon>
          <span>任务看板</span>
        </el-menu-item>
        <el-menu-item index="/bugs">
          <el-icon><Warning /></el-icon>
          <span>缺陷</span>
        </el-menu-item>
        <el-menu-item index="/sprints">
          <el-icon><Calendar /></el-icon>
          <span>迭代</span>
        </el-menu-item>
        <el-menu-item index="/ai-assistant">
          <el-icon><MagicStick /></el-icon>
          <span>AI 助手</span>
        </el-menu-item>
        <el-menu-item index="/profile">
          <el-icon><User /></el-icon>
          <span>个人资料</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container class="main-container">
      <el-header class="main-header">
        <div class="header-left"></div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              {{ currentUserName }}
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人资料</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  DataLine, Folder, List, Warning, Calendar, User, ArrowDown, MagicStick
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const activeMenu = ref(route.path)
const currentUserName = computed(() => authStore.currentUser?.nickname || authStore.currentUser?.username || '用户')

watch(() => route.path, (newPath) => {
  activeMenu.value = newPath
})

const handleCommand = async (command: string) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定退出登录吗？', '提示', { type: 'warning' })
      authStore.logout()
      router.push('/login')
      ElMessage.success('已退出')
    } catch {}
  } else if (command === 'profile') {
    router.push('/profile')
  }
}
</script>

<style scoped lang="scss">
.main-layout {
  height: 100vh;
  .main-aside {
    background-color: #545c64;
    .logo {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      padding: 20px;
      color: white;
      font-size: 18px;
      font-weight: bold;
      border-bottom: 1px solid rgba(255,255,255,0.1);
      .logo-icon {
        width: 32px;
        height: 32px;
        background-color: #409eff;
        border-radius: 4px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
    .menu {
      border-right: none;
    }
  }
  .main-container {
    .main-header {
      background-color: white;
      border-bottom: 1px solid #ebeef5;
      display: flex;
      justify-content: flex-end;
      align-items: center;
      padding: 0 20px;
      .user-info {
        cursor: pointer;
        &:hover { color: #409eff; }
      }
    }
    .main-content {
      background-color: #f0f2f5;
      padding: 20px;
    }
  }
}
</style>