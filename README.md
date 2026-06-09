# TaskHub Web - 前端工程

一个基于 **Vue 3 + TypeScript + Vite** 的完整前端工程，与 TaskHub 后端无缝对接。

## 🚀 快速开始

### 系统要求
- Node.js >= 16
- npm 或 pnpm

### 安装依赖

```bash
# 使用 npm
npm install

# 或使用 pnpm (推荐)
pnpm install
```

### 开发服务器

```bash
npm run dev
# 或
pnpm dev
```

默认访问地址: `http://localhost:5173`

### 生产构建

```bash
npm run build
# 或
pnpm build
```

### 预览生产构建

```bash
npm run preview
# 或
pnpm preview
```

## 📁 项目结构

```
taskhub-web/
├── src/
│   ├── api/                 # API 接口层
│   │   ├── auth.ts         # 认证接口
│   │   ├── project.ts      # 项目接口
│   │   ├── task.ts         # 任务接口
│   │   ├── sprint.ts       # Sprint接口
│   │   └── bug.ts          # 缺陷接口
│   ├── components/          # 可复用组件
│   ├── layouts/             # 布局组件
│   │   ├── MainLayout.vue  # 主框架布局
│   │   └── EmptyLayout.vue # 空布局（登录页面使用）
│   ├── router/              # 路由配置
│   │   ├── index.ts        # 路由入口
│   │   └── routes.ts       # 路由定义
│   ├── stores/              # Pinia 状态管理
│   │   ├── auth.ts         # 认证状态
│   │   ├── project.ts      # 项目状态
│   │   ├── task.ts         # 任务状态
│   │   ├── sprint.ts       # Sprint状态
│   │   └── bug.ts          # 缺陷状态
│   ├── types/               # TypeScript 类型定义
│   │   ├── auth.ts
│   │   ├── common.ts
│   │   ├── project.ts
│   │   ├── task.ts
│   │   └── sprint.ts
│   ├── utils/               # 工具函数
│   │   └── request.ts      # Axios 二次封装
│   ├── views/               # 页面组件
│   │   ├── login/          # 登录页面
│   │   ├── dashboard/      # 仪表板
│   │   ├── project/        # 项目管理
│   │   ├── task/           # Task看板
│   │   ├── sprint/         # Sprint管理
│   │   └── bug/            # 缺陷管理
│   ├── App.vue              # 根组件
│   ├── main.ts              # 入口文件
│   └── style.css            # 全局样式
├── vite.config.ts           # Vite 配置
├── tsconfig.json            # TypeScript 配置
└── package.json             # 项目依赖
```

## 📦 核心技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5+ | 渐进式前端框架 |
| TypeScript | ~6.0 | 类型检查语言 |
| Vite | 8.0+ | 新一代前端构建工具 |
| Pinia | 3.0+ | Vue 3 状态管理 |
| Vue Router | 5.1+ | 路由管理 |
| Axios | 1.16+ | HTTP 请求库 |
| Element Plus | 2.14+ | Vue 3 UI 组件库 |

## 🔑 核心功能

### 1. 认证系统
- ✅ JWT Token 登录
- ✅ Token 自动刷新
- ✅ 请求拦截器自动携带 Token
- ✅ 响应拦截器自动处理错误
- ✅ 路由守卫权限控制

### 2. 项目管理
- ✅ 创建/编辑/删除项目
- ✅ 项目列表展示
- ✅ 仪表板快速访问

### 3. Task 看板
- ✅ Kanban 视图（待办 → 进行中 → 已完成）
- ✅ 创建/编辑/删除任务
- ✅ 任务状态流转
- ✅ 项目过滤

### 4. Sprint 管理
- ✅ 创建/编辑/删除 Sprint
- ✅ Sprint 列表管理
- ✅ 日期范围设置

### 5. 缺陷管理
- ✅ 缺陷报告
- ✅ 严重程度分类（P0/P1/P2）
- ✅ 缺陷追踪

### 6. 仪表板
- ✅ 数据统计卡片
- ✅ 最近项目/任务展示
- ✅ 任务完成情况概览

## 🔌 API 集成

所有 API 调用都在 `src/api/` 目录中定义，支持：
- 自动 Token 携带
- 错误统一处理
- 响应数据格式化

### API 基础配置

```typescript
// src/utils/request.ts
const request = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 10000
})
```

### 后端对接

确保后端服务运行在 `http://localhost:8080`，CORS 已配置允许 `http://localhost:3000` 和 `http://localhost:5173`

## 🏠 主要页面

### 1. 登录页 (`/login`)
- 用户名密码登录
- 表单验证
- Token 存储

**测试账号:**
- 用户名: `admin`
- 密码: `123456`

### 2. 仪表板 (`/dashboard`)
- 项目总数
- 任务总数
- 已完成/进行中任务
- 最近项目和任务

### 3. 项目管理 (`/projects`)
- 项目CRUD操作
- 项目列表展示

### 4. Task看板 (`/kanban`)
- Kanban 式看板
- 拖拽式状态管理
- 快速创建/编辑任务

### 5. Sprint管理 (`/sprints`)
- Sprint列表
- 创建/编辑Sprint
- 日期管理

### 6. 缺陷管理 (`/bugs`)
- 缺陷报告
- 严重程度管理
- 缺陷追踪

## 🛡️ 路由守卫

- 未登录用户无法访问受保护的页面，自动重定向到登录页
- 已登录用户访问登录页会重定向到仪表板
- 支持路由元数据自定义权限

## 📋 状态管理 (Pinia)

每个模块都有对应的 Store：

```typescript
// 使用示例
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
await authStore.login({ username: 'admin', password: '123456' })
```

## 🔐 安全性

- ✅ JWT Token 存储在 localStorage（生产环境建议使用更安全的方案）
- ✅ Token 过期自动刷新
- ✅ 请求拦截器自动携带 Token
- ✅ 响应拦截器处理 401/403 错误
- ✅ 敏感操作前确认

## 🎨 样式系统

- 使用 Element Plus 组件库
- SCSS 预处理器
- 响应式设计
- 全局样式变量

## 📱 浏览器支持

- Chrome (最新版)
- Firefox (最新版)
- Safari (最新版)
- Edge (最新版)

## ⚙️ 配置说明

### 修改后端地址

编辑 `src/utils/request.ts`:

```typescript
const request = axios.create({
  baseURL: 'http://your-backend-url/api/v1', // 修改这里
  timeout: 10000
})
```

### 修改 CORS

如需修改后端 CORS 配置：

编辑后端的 `CorsConfig.java`:

```java
registry.addMapping("/api/**")
        .allowedOrigins("http://localhost:5173") // 修改前端地址
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
```

## 🐛 常见问题

### Q: 登录失败提示"操作失败"
**A:** 确保后端服务已启动，且配置的后端地址正确

### Q: 加载数据为空
**A:** 检查 Token 是否过期，查看浏览器控制台错误信息

### Q: CORS 错误
**A:** 确保后端 CORS 配置正确，允许了当前前端地址

## 📚 相关文档

- [Vue 3 官方文档](https://vuejs.org/)
- [TypeScript 官方文档](https://www.typescriptlang.org/)
- [Vite 官方文档](https://vitejs.dev/)
- [Pinia 官方文档](https://pinia.vuejs.org/)
- [Element Plus 官方文档](https://element-plus.org/)
- [Vue Router 官方文档](https://router.vuejs.org/)

## 📝 开发建议

1. **组件化开发**: 在 `src/components/` 中创建可复用组件
2. **API 管理**: 所有 API 调用在 `src/api/` 中集中管理
3. **状态管理**: 使用 Pinia Store 管理全局状态
4. **类型安全**: 充分利用 TypeScript 类型检查
5. **代码规范**: 遵循 Vue 3 Composition API 最佳实践

## 📄 许可证

MIT License

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！
