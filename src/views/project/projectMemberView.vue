<template>
  <div class="project-member-container">
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span>项目成员</span>
          <el-button type="primary" size="small" @click="openAddDialog">添加成员</el-button>
        </div>
      </template>

      <!-- 提示信息 -->
      <el-alert
        title="如何添加成员？"
        type="info"
        :closable="false"
        show-icon
        style="margin-bottom: 16px"
      >
        <template #default>
          需要添加的用户ID可以在该用户的<strong>个人资料</strong>页面中找到，复制后粘贴到下方输入框即可。
        </template>
      </el-alert>

      <el-table :data="members" v-loading="loading" stripe>
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="role" label="角色">
          <template #default="{ row }">
            <el-select v-model="row.role" size="small" @change="updateRole(row)" :disabled="!isAdminOrOwner">
              <el-option label="管理员" value="ADMIN" />
              <el-option label="成员" value="MEMBER" />
              <el-option label="观察者" value="VIEWER" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button link type="danger" size="small" @click="removeMember(row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加成员对话框 -->
    <el-dialog title="添加成员" v-model="addDialogVisible" width="400px">
      <el-form :model="addForm" label-width="80px">
        <el-form-item label="用户ID" required>
          <!-- 关键：使用 v-model 绑定字符串，不要使用 .number -->
          <el-input
            v-model="addForm.userId"
            placeholder="请输入用户ID（如：397856744774176768）"
            clearable
          />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="addForm.role">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="成员" value="MEMBER" />
            <el-option label="观察者" value="VIEWER" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addMember" :loading="adding">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { projectMemberApi } from '@/api/projectMember'

const route = useRoute()
const projectId = computed(() => route.params.id as string)

const members = ref<any[]>([])
const loading = ref(false)
const addDialogVisible = ref(false)
const adding = ref(false)

// 表单数据：userId 为字符串类型
const addForm = ref({
  userId: '',      // 字符串，不是数字
  role: 'MEMBER'
})

// 权限判断（简化：只要当前用户是项目所有者或是管理员即可编辑）
// 实际可根据返回的成员角色判断，这里简单设为 true（仅用于演示）
const isAdminOrOwner = ref(true)

// 获取成员列表
const fetchMembers = async () => {
  loading.value = true
  try {
    const data = await projectMemberApi.getMembers(projectId.value)
    members.value = data
  } catch (error) {
    ElMessage.error('获取成员列表失败')
  } finally {
    loading.value = false
  }
}

// 更新角色
const updateRole = async (row: any) => {
  try {
    await projectMemberApi.updateRole(projectId.value, row.userId, { role: row.role })
    ElMessage.success('角色更新成功')
  } catch {
    ElMessage.error('更新失败')
    await fetchMembers() // 回滚
  }
}

// 移除成员
const removeMember = async (row: any) => {
  await ElMessageBox.confirm(`确定移除成员 ${row.nickname || row.username} 吗？`, '提示', { type: 'warning' })
  try {
    await projectMemberApi.removeMember(projectId.value, row.userId)
    ElMessage.success('移除成功')
    await fetchMembers()
  } catch {
    ElMessage.error('移除失败')
  }
}

// 打开添加对话框
const openAddDialog = () => {
  addForm.value = { userId: '', role: 'MEMBER' }
  addDialogVisible.value = true
}

// 添加成员
const addMember = async () => {
  const userIdStr = addForm.value.userId.trim()
  if (!userIdStr) {
    ElMessage.error('请输入用户ID')
    return
  }

  // 检查是否为纯数字（雪花ID只包含数字）
  if (!/^\d+$/.test(userIdStr)) {
    ElMessage.error('用户ID格式错误，应为纯数字')
    return
  }

  adding.value = true
  try {
    // 关键：传递字符串，后端 Long 可以正确转换
    await projectMemberApi.addMember(projectId.value, {
      userId: userIdStr,   // 字符串形式
      role: addForm.value.role
    })
    ElMessage.success('添加成功')
    addDialogVisible.value = false
    await fetchMembers()
  } catch (error: any) {
    const msg = error.response?.data?.message || error.message || '添加失败'
    ElMessage.error(msg)
  } finally {
    adding.value = false
  }
}

onMounted(() => {
  fetchMembers()
})
</script>

<style scoped lang="scss">
.project-member-container {
  padding: 20px;
}
</style>