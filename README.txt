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
