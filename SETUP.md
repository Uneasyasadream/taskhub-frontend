# 🚀 TaskHub 前端项目 - 快速开始指南

## 📋 项目完成情况总览

你现在拥有一个**完整可运行的 Vue 3 前端工程**，包含以下所有功能：

### ✅ 已完成的功能清单

#### 1. **核心认证系统** ✅
- JWT Token 登录/注册
- Token 自动存储和刷新
- 请求拦截器自动携带 Token
- Token 过期自动跳转登录页
- 路由守卫权限控制

#### 2. **API 接口层** ✅
- `src/api/auth.ts` - 认证API
- `src/api/project.ts` - 项目API（CRUD + 分页）
- `src/api/task.ts` - 任务API（CRUD + 项目过滤）
- `src/api/sprint.ts` - SprintAPI（CRUD + 项目过滤）
- `src/api/bug.ts` - 缺陷API（CRUD + 项目过滤）

#### 3. **状态管理** ✅
- `src/stores/auth.ts` - 认证状态、Token管理、用户信息
- `src/stores/project.ts` - 项目列表、详情、CRUD
- `src/stores/task.ts` - 任务管理、按状态分组、Kanban数据
- `src/stores/sprint.ts` - Sprint管理
- `src/stores/bug.ts` - 缺陷管理

#### 4. **路由和导航** ✅
- 全局路由守卫
- 权限控制（需要登录vs公开页面）
- 页面标题自动设置
- 路由元数据支持

#### 5. **用户界面** ✅
- **MainLayout.vue** - 主框架、侧边栏菜单、顶部导航栏、用户菜单
- **LoginView.vue** - 登录页面（集成验证、错误处理）
- **DashboardView.vue** - 仪表板（统计卡片、数据概览）
- **ProjectView.vue** - 项目管理（列表、创建、编辑、删除）
- **TaskView.vue** - Kanban看板（三列状态管理、拖拽式操作）
- **SprintView.vue** - Sprint管理界面
- **BugView.vue** - 缺陷管理界面

#### 6. **HTTP 请求处理** ✅
- Axios 二次封装
- 请求拦截器
- 响应拦截器
- 错误统一处理
- Token 自动携带
- 业务错误提示

#### 7. **UI组件库** ✅
- Element Plus 集成
- 全局样式定制
- 响应式设计
- 弹窗、表格、表单等组件

---

## 🔧 运行前准备

### 1. 确保后端已启动

```bash
# 后端项目应运行在 http://localhost:8080
# 确保 CORS 已配置，允许 http://localhost:5173
```

### 2. 安装依赖

```bash
cd d:/SpringBoot/vue/taskhub-web

# 使用 npm
npm install

# 或使用 pnpm (推荐,更快)
pnpm install
```

### 3. 启动开发服务器

```bash
npm run dev
# 或
pnpm dev
```

输出示例：
```
  VITE v8.0.12  ready in 1234 ms

  ➜  Local:   http://localhost:5173/
  ➜  press h + enter to show help
```

### 4. 访问应用

打开浏览器，访问：**http://localhost:5173**

---

## 📱 登录和测试

### 测试账户

```
用户名: admin
密码: 123456
```

### 功能流程

1. **登录** → 输入用户名和密码
2. **进入仪表板** → 查看数据统计
3. **创建项目** → 项目管理页面创建新项目
4. **创建任务** → Kanban看板创建任务
5. **管理Sprint** → Sprint页面创建和管理Sprint
6. **报告缺陷** → 缺陷页面报告和追踪缺陷

---

## 📁 项目结构说明

```
taskhub-web/
├── src/
│   ├── api/              # ✅ API 接口模块
│   ├── assets/           # 静态资源
│   ├── components/       # 可复用组件
│   ├── layouts/          # ✅ 布局组件
│   ├── router/           # ✅ 路由配置
│   ├── stores/           # ✅ Pinia 状态管理
│   ├── types/            # ✅ TypeScript 类型定义
│   ├── utils/            # ✅ 工具函数
│   ├── views/            # ✅ 页面视图
│   ├── App.vue           # ✅ 根组件
│   ├── main.ts           # ✅ 入口文件
│   └── style.css         # ✅ 全局样式
├── public/               # 公共资源
├── vite.config.ts        # ✅ Vite 配置
├── tsconfig.json         # ✅ TypeScript 配置
├── package.json          # ✅ 项目依赖
└── README.md             # 详细文档
```

---

## 🔗 后端对接检查表

- [ ] 后端服务运行在 `http://localhost:8080`
- [ ] CORS 配置允许 `http://localhost:5173`
- [ ] 数据库已初始化，表结构正确
- [ ] 测试账户存在 (admin/123456)
- [ ] 所有API接口可正常访问

### 后端CORS配置示例

```java
// src/main/java/com/taskhub/ai/config/CorsConfig.java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:3000",
                    "http://localhost:5173"  // 前端地址
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

---

## 🐛 常见问题和解决方案

### Q1: 登录失败，提示"操作失败"
**A:** 
- 检查后端是否启动在 `http://localhost:8080`
- 查看浏览器控制台（F12 → Console）的错误信息
- 确保 CORS 已正确配置
- 确保测试账户在数据库中存在

### Q2: "网络错误" 或 "无法连接到服务器"
**A:**
- 后端服务是否正在运行？
- 后端地址是否正确？（默认：http://localhost:8080）
- 防火墙是否阻止了连接？
- 检查浏览器开发工具（F12）的 Network 标签

### Q3: 加载页面数据为空
**A:**
- 检查 Token 是否已正确保存（F12 → Application → LocalStorage）
- 检查 Token 是否过期
- 检查浏览器控制台是否有错误
- 确保后端API正确返回数据

### Q4: CORS 错误："Access to XMLHttpRequest ... blocked by CORS policy"
**A:**
- 后端 CORS 配置中要包含 `http://localhost:5173`
- 重启后端服务以应用 CORS 配置更改
- 清空浏览器缓存
- 检查后端控制台是否有错误信息

### Q5: 项目/任务列表为空
**A:**
- 使用 Postman 或其他工具测试后端API
- 确保数据库中有相应的数据
- 检查用户权限（某些API可能需要特定权限）

---

## 💻 开发命令

```bash
# 安装依赖
pnpm install

# 启动开发服务器 (推荐)
pnpm dev

# 生产构建
pnpm build

# 预览生产构建
pnpm preview

# 类型检查
pnpm run type-check
```

---

## 🎨 主要技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.5+ | 前端框架 |
| TypeScript | ~6.0 | 类型检查 |
| Vite | 8.0+ | 构建工具 |
| Pinia | 3.0+ | 状态管理 |
| Vue Router | 5.1+ | 路由管理 |
| Axios | 1.16+ | HTTP请求 |
| Element Plus | 2.14+ | UI组件库 |

---

## 📝 文件修改记录

### 已修改/创建的文件：

```
✅ src/api/auth.ts - 认证API
✅ src/api/project.ts - 项目API
✅ src/api/task.ts - 任务API
✅ src/api/sprint.ts - SprintAPI
✅ src/api/bug.ts - 缺陷API
✅ src/stores/auth.ts - 认证状态
✅ src/stores/project.ts - 项目状态
✅ src/stores/task.ts - 任务状态
✅ src/stores/sprint.ts - Sprint状态
✅ src/stores/bug.ts - 缺陷状态
✅ src/types/* - 类型定义
✅ src/router/index.ts - 路由配置
✅ src/router/routes.ts - 路由定义
✅ src/layouts/MainLayout.vue - 主布局
✅ src/layouts/EmptyLayout.vue - 空布局
✅ src/views/login/LoginView.vue - 登录页
✅ src/views/dashboard/DashboardView.vue - 仪表板
✅ src/views/project/ProjectView.vue - 项目管理
✅ src/views/task/TaskView.vue - Task看板
✅ src/views/sprint/SprintView.vue - Sprint管理
✅ src/views/bug/BugView.vue - 缺陷管理
✅ src/utils/request.ts - Axios二次封装
✅ src/style.css - 全局样式
✅ README.md - 项目文档
```

---

## 🎯 后续功能扩展建议

1. **高级功能**
   - 任务搜索和过滤
   - 用户权限管理
   - 团队协作功能
   - 文件上传附件

2. **优化**
   - 虚拟滚动（大数据列表）
   - 本地缓存策略
   - 离线模式支持
   - PWA转换

3. **完善**
   - 单元测试
   - E2E测试
   - 性能监控
   - 错误追踪

---

## 📞 技术支持

如遇到问题，请检查：
1. 后端服务是否运行
2. CORS 配置是否正确
3. 浏览器控制台错误信息
4. 网络请求（Network 标签）

---

**祝你使用愉快！🎉**

