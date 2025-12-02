<<<<<<< HEAD
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
=======
功能特性;## Features
🚫 聊天拦截
默认关闭普通聊天功能;Default disables ordinary chat functionality

拦截未经许可的聊天消息;Intercepts unauthorized chat messages

例如：输入 你好 会被拦截当设置前缀为 " 时，输入 "你好 聊天栏输出 你好

Example: Typing Hello will be intercepted; when the prefix is set to ", typing "Hello will output Hello in chat.

🔑 前缀系统
可自定义聊天前缀（如"、 '、!、say_ 等）;Customizable chat prefixes

支持前缀绑定/取消;Supports prefix binding/unbinding

兼容 Baritone 的 # 命令系统;Compatible with Baritone's # command system

⚙️ 客户端管理
/chat 命令管理系统;/chat command management system

设置本地保存;Settings saved locally

纯客户端运行，不影响服务器;Runs purely on the client side, does not affect the server

🎮 兼容性
完全兼容 Baritone 模组;Fully compatible with Baritone mod

支持所有 Minecraft 命令;Supports all Minecraft commands

单人游戏和多人游戏都适用;Applicable to both single-player and multiplayer games

安装方法;## Installation
前置要求;### Prerequisites
Minecraft 1.12.2

Forge 14.23.5.2860 或更高版本;Forge 14.23.5.2860 or higher

安装步骤;### Installation Steps
下载最新版本的 chatinterceptor-x.x.x.jar;Download the latest version of chatinterceptor-x.x.x.jar

将文件放入 .minecraft/mods/ 文件夹;Place the file into the .minecraft/mods/ folder

启动 Minecraft 1.12.2 Forge 客户端;Launch the Minecraft 1.12.2 Forge client

使用方法;## Usage

基本命令;### Basic Commands
/chat set on          # 开启聊天;Enable chat
/chat set off         # 关闭聊天;Disable chat
/chat set key <前缀>   # 设置聊天前缀;Set chat prefix
/chat info            # 查看当前设置;View current settings
>>>>>>> 5c194e579f2332dd71c85616b77c75107395cecb
