<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header><span>个人资料</span></template>
      <el-form :model="form" label-width="100px" ref="formRef">
        <el-form-item label="用户ID">
          <el-input v-model="form.id" disabled>
            <template #append>
              <el-button @click="copyId" :icon="CopyDocument">复制</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="/api/upload/avatar"
            :headers="{ Authorization: `Bearer ${authStore.accessToken}` }"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave" :loading="saving">保存修改</el-button>
          <el-button @click="openPasswordDialog">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 修改密码对话框 -->
    <el-dialog title="修改密码" v-model="passwordDialogVisible" width="400px">
      <el-form :model="passwordForm" label-width="100px" ref="passwordFormRef" :rules="passwordRules">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input type="password" v-model="passwordForm.oldPassword" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" v-model="passwordForm.newPassword" />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input type="password" v-model="passwordForm.confirmPassword" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePasswordChange" :loading="changingPassword">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, CopyDocument } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { userApi } from '@/api/user'
import type { FormInstance, FormRules } from 'element-plus'

const authStore = useAuthStore()

const form = reactive({
  id: '',
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: ''
})
const saving = ref(false)
const passwordDialogVisible = ref(false)
const changingPassword = ref(false)
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
const passwordFormRef = ref<FormInstance>()

const passwordRules: FormRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 复制用户ID
const copyId = async () => {
  try {
    await navigator.clipboard.writeText(form.id)
    ElMessage.success('用户ID已复制到剪贴板')
  } catch {
    ElMessage.error('复制失败，请手动复制')
  }
}

const loadUserInfo = async () => {
  if (authStore.currentUser) {
    form.id = String(authStore.currentUser.id)
    form.username = authStore.currentUser.username
    form.nickname = authStore.currentUser.nickname || ''
    form.email = authStore.currentUser.email || ''
    form.phone = authStore.currentUser.phone || ''
    form.avatar = authStore.currentUser.avatar || ''
  } else {
    const data = await userApi.getCurrentUser()
    form.id = String(data.id)
    form.username = data.username
    form.nickname = data.nickname || ''
    form.email = data.email || ''
    form.phone = data.phone || ''
    form.avatar = data.avatar || ''
  }
}

const handleSave = async () => {
  saving.value = true
  try {
    await authStore.updateProfile({
      nickname: form.nickname,
      email: form.email,
      phone: form.phone
    })
    ElMessage.success('保存成功')
  } catch {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

const openPasswordDialog = () => {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordDialogVisible.value = true
}

const handlePasswordChange = async () => {
  if (!passwordFormRef.value) return
  await passwordFormRef.value.validate()
  changingPassword.value = true
  try {
    await authStore.changePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    ElMessage.success('密码修改成功，请重新登录')
    passwordDialogVisible.value = false
    setTimeout(() => {
      authStore.logout()
      window.location.href = '/login'
    }, 1500)
  } catch {
    ElMessage.error('修改失败，请检查原密码')
  } finally {
    changingPassword.value = false
  }
}

const handleAvatarSuccess = async (response: string) => {
  form.avatar = response
  await authStore.updateAvatar(response)
  ElMessage.success('头像更新成功')
}

const beforeAvatarUpload = (file: File) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isJPG) ElMessage.error('只能上传 JPG/PNG 图片')
  if (!isLt2M) ElMessage.error('图片大小不能超过 2MB')
  return isJPG && isLt2M
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile-container {
  display: flex;
  justify-content: center;
  padding: 40px;
}
.profile-card {
  width: 600px;
}
.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}
.avatar-uploader .avatar-uploader-icon {
  font-size: 28px;
  width: 100px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
}
</style>