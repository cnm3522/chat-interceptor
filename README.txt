# Chat Interceptor Mod

Minecraft 1.12.2 客户端模组 - 聊天拦截管理系统

## 功能特性
- 🚫 智能聊天拦截系统
- 🔑 按键/手动双模式前缀输入
- 🎮 原生键位设置支持
- ⚡ 兼容 Baritone (#命令)
- 💾 本地配置保存

## 安装
1. 下载 `chatinterceptor-1.2.0.jar`
2. 放入 `.minecraft/mods/`
3. 启动 Minecraft 1.12.2 Forge

## 使用
### 基本命令
/chat set on/off # 开启/关闭聊天
/chat set key <前缀> # 设置手动前缀
/chat key info # 查看按键设置
/chat info # 查看所有设置

text

### 按键设置
1. 游戏菜单 → 选项 → 控制
2. 找到"聊天拦截器"分类
3. 设置"输入聊天前缀"按键

### 按键功能
- 默认按键：`'` (单引号)
- 按下自动打开聊天框并输入前缀
- 可设置为 NONE 禁用
📦 构建说明
放置所有文件到正确位置

运行构建命令：

bash
gradlew.bat clean build --no-daemon -Dorg.gradle.jvmargs="-Xmx512m"
构建输出：build/libs/chatinterceptor-1.2.0.jar