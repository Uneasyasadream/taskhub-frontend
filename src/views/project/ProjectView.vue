<template>
  <div>
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span>我的项目</span>
          <el-button type="primary" @click="openDialog()">创建项目</el-button>
        </div>
      </template>

      <el-table :data="projectStore.projects" v-loading="projectStore.isLoading" stripe>
        <el-table-column prop="name" label="项目名称" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="createdTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openDialog(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
            <el-button link type="info" size="small" @click="goToMembers(row)">成员管理</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 创建/编辑对话框 -->
    <el-dialog :title="editId ? '编辑项目' : '创建项目'" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px" ref="formRef" :rules="rules">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入项目描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveProject" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useProjectStore } from '@/stores/project'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import type { ProjectVO } from '@/types/project'

const router = useRouter()
const projectStore = useProjectStore()

const dialogVisible = ref(false)
const editId = ref<string | number | null>(null)
const saving = ref(false)
const formRef = ref<FormInstance>()

const form = reactive({ name: '', description: '' })

const rules: FormRules = {
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }]
}

const openDialog = (row?: ProjectVO) => {
  if (row) {
    editId.value = row.id
    form.name = row.name
    form.description = row.description || ''
  } else {
    editId.value = null
    form.name = ''
    form.description = ''
  }
  dialogVisible.value = true
}

const saveProject = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  saving.value = true
  try {
    if (editId.value) {
      await projectStore.updateProject(editId.value, { name: form.name, description: form.description })
    } else {
      await projectStore.createProject({ name: form.name, description: form.description })
    }
    dialogVisible.value = false
  } catch (error) {
    // 已处理
  } finally {
    saving.value = false
  }
}

const handleDelete = async (row: ProjectVO) => {
  if (!row || !row.id) {
    ElMessage.error('无效的项目数据')
    return
  }
  try {
    await ElMessageBox.confirm(`确定要删除项目“${row.name}”吗？删除后不可恢复。`, '警告', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await projectStore.deleteProject(String(row.id))
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

const goToMembers = (row: ProjectVO) => {
  router.push(`/projects/${row.id}/members`)
}

onMounted(() => {
  projectStore.fetchProjects()
})
</script>