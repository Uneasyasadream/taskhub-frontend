<template>
  <div class="ai-assistant-container">
    <el-card class="chat-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>🤖 AI 智能助手</span>
          <el-button type="danger" size="small" plain @click="clearChat" :disabled="messages.length === 0">
            清空对话
          </el-button>
        </div>
      </template>

      <!-- 聊天消息区域 -->
      <div class="chat-messages" ref="messagesContainer">
        <div v-for="(msg, idx) in messages" :key="idx" :class="['message', msg.role]">
          <div class="message-avatar">
            <el-avatar :size="32" :icon="msg.role === 'user' ? UserFilled : Service" />
          </div>
          <div class="message-content">
            <div class="message-text" v-html="formatMessage(msg.content)"></div>
            <div class="message-time">{{ msg.timestamp }}</div>
          </div>
        </div>
        <div v-if="loading" class="message assistant">
          <div class="message-avatar">
            <el-avatar :size="32" :icon="Service" />
          </div>
          <div class="message-content">
            <div class="message-text typing">AI 正在思考中<span>.</span><span>.</span><span>.</span></div>
          </div>
        </div>
      </div>

      <!-- 预置提示词模板 -->
      <div class="preset-prompts">
        <el-button
          v-for="prompt in presetPrompts"
          :key="prompt.label"
          size="small"
          plain
          @click="sendPresetPrompt(prompt.text)"
        >
          {{ prompt.label }}
        </el-button>
      </div>

      <!-- 输入区域 -->
      <div class="chat-input">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="输入你的问题，例如：帮我拆分任务：开发登录模块"
          @keydown.ctrl.enter="sendMessage"
        />
        <div class="input-actions">
          <el-button type="primary" @click="sendMessage" :loading="loading" :disabled="!inputMessage.trim()">
            发送
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { UserFilled, Service } from '@element-plus/icons-vue'
import request from '@/api/request'   // 使用项目封装的 request 实例，会自动携带 token

interface Message {
  role: 'user' | 'assistant'
  content: string
  timestamp: string
}

const messages = ref<Message[]>([])
const inputMessage = ref('')
const loading = ref(false)
const messagesContainer = ref<HTMLElement | null>(null)

// 预置提示词模板
const presetPrompts = [
  { label: '📋 帮我拆分任务', text: '帮我拆分任务：开发用户管理模块，包括列表、新增、编辑、删除功能' },
  { label: '📊 总结这个迭代', text: '总结一个两周的敏捷迭代，主要完成了登录功能、任务看板、缺陷修复，遇到了数据库连接问题' },
  { label: '🐛 缺陷分析建议', text: '分析这个缺陷：用户点击登录按钮后没有任何响应，控制台报错500' },
  { label: '📝 生成任务描述', text: '为任务"实现JWT token刷新机制"生成详细的技术描述和验收标准' },
  { label: '🚀 项目建议', text: '我们的项目是一个任务管理系统，请给出一些提升开发效率的建议' }
]

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

// 格式化消息（支持简单的换行）
const formatMessage = (content: string) => {
  return content.replace(/\n/g, '<br>')
}

// 发送消息
const sendMessage = async () => {
  const prompt = inputMessage.value.trim()
  if (!prompt) return

  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: prompt,
    timestamp: new Date().toLocaleTimeString()
  })
  inputMessage.value = ''
  await scrollToBottom()

  loading.value = true
  try {
    // 注意：request 的 baseURL 已经配置为 '/api'，代理到后端 '/api/v1'
    // 所以实际请求路径是 '/api/ai/generate'，这里只需写 '/ai/generate'
    const response = await request.post('/ai/generate', { prompt })
    // 后端返回格式为 Result<T>，所以 response.data 是实际内容
    const aiResponse = (response as any).data || response
    messages.value.push({
      role: 'assistant',
      content: aiResponse,
      timestamp: new Date().toLocaleTimeString()
    })
  } catch (error: any) {
    console.error('AI 请求失败:', error)
    const errorMsg = error.response?.data?.message || error.message || 'AI 请求失败，请稍后重试'
    ElMessage.error(errorMsg)
  } finally {
    loading.value = false
    await scrollToBottom()
  }
}

// 使用预置提示词
const sendPresetPrompt = (text: string) => {
  inputMessage.value = text
  sendMessage()
}

// 清空对话
const clearChat = () => {
  messages.value = []
  ElMessage.success('对话已清空')
}

// 欢迎消息
onMounted(() => {
  messages.value.push({
    role: 'assistant',
    content: '你好！我是 TaskHub AI 助手 🤖\n\n我可以帮你：\n- 拆分任务\n- 总结迭代\n- 分析缺陷\n- 生成任务描述\n- 提供项目建议\n\n试试点击上面的预设按钮或直接输入问题吧！',
    timestamp: new Date().toLocaleTimeString()
  })
})
</script>

<style scoped lang="scss">
.ai-assistant-container {
  padding: 20px;
  height: calc(100vh - 100px);

  .chat-card {
    height: 100%;
    display: flex;
    flex-direction: column;

    :deep(.el-card__body) {
      flex: 1;
      display: flex;
      flex-direction: column;
      overflow: hidden;
    }
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 18px;
    font-weight: bold;
  }

  .chat-messages {
    flex: 1;
    overflow-y: auto;
    padding: 16px;
    background-color: #f5f7fa;
    border-radius: 8px;
    margin-bottom: 16px;

    .message {
      display: flex;
      margin-bottom: 20px;
      gap: 12px;

      &.user {
        flex-direction: row-reverse;

        .message-content {
          background-color: #409eff;
          color: white;
        }
      }

      &.assistant {
        .message-content {
          background-color: white;
          color: #303133;
        }
      }

      .message-avatar {
        flex-shrink: 0;
      }

      .message-content {
        max-width: 70%;
        padding: 12px 16px;
        border-radius: 12px;
        box-shadow: 0 1px 2px rgba(0,0,0,0.1);

        .message-text {
          line-height: 1.5;
          word-break: break-word;
          white-space: pre-wrap;
        }

        .message-time {
          font-size: 11px;
          margin-top: 6px;
          opacity: 0.7;
          text-align: right;
        }

        .typing span {
          animation: blink 1.4s infinite;
          &:nth-child(2) { animation-delay: 0.2s; }
          &:nth-child(3) { animation-delay: 0.4s; }
        }
      }
    }
  }

  .preset-prompts {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 16px;
    padding: 8px 0;
    border-top: 1px solid #e4e7ed;
    border-bottom: 1px solid #e4e7ed;
  }

  .chat-input {
    .input-actions {
      display: flex;
      justify-content: flex-end;
      margin-top: 12px;
    }
  }
}

@keyframes blink {
  0%, 100% { opacity: 0; }
  50% { opacity: 1; }
}
</style>