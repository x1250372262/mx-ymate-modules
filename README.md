# 🧩 mx-ymate-modules

> 🚀 A modular rapid development toolkit based on **YMP (ymate-platform-v2)** framework.  
> 🚀 基于 **YMP（ymate-platform-v2）** 框架的模块化快速开发组件集。

`mx-ymate-modules` provides a collection of cohesive, pluggable modules to help Java developers quickly build robust backend systems.  
`mx-ymate-modules` 提供了一系列高内聚、可插拔的功能模块，帮助 Java 开发者快速构建高质量的后端系统。

---

## 📦 Modules Overview / 模块说明

| Module | Description |
|--------|--------------|
| **mx-ymate-clickhouse** | Encapsulates ClickHouse for high-performance data storage and queries.<br/>封装 ClickHouse，用于海量数据的高性能存储与查询。 |
| **mx-ymate-dev** | Core module providing common utilities, unified result objects, and base components.<br/>核心模块，提供常用工具类、通用返回结果等基础功能。 |
| **mx-ymate-excel** | Excel import and export utilities.<br/>提供 Excel 导入导出功能。 |
| **mx-ymate-manager-templates** | Backend admin page templates (non-Vue implementation).<br/>后台管理模板（非 Vue 实现）。 |
| **mx-ymate-maven-plugin** | Maven plugin for generating code based on configuration files, with customizable templates.<br/>Maven 插件，可根据配置文件生成代码，模板可自定义。 |
| **mx-ymate-monitor** | System and server monitoring module.<br/>系统与服务器监控模块。 |
| **mx-ymate-mqtt** | MQTT module supporting publish/subscribe and Paho clients.<br/>MQTT 模块，支持发布/订阅，兼容 Paho 等客户端。 |
| **mx-ymate-netty** | Netty communication module with built-in heartbeat detection, can act as both server and client.<br/>Netty 通信模块，内置心跳检测，可当服务端或客户端使用。 |
| **mx-ymate-qwen** | LLM module based on Alibaba’s Qwen API.<br/>基于阿里通义千问封装的 LLM 模块。 |
| **mx-ymate-redis** | Redis integration module with simplified API operations.<br/>Redis 整合模块，封装常用操作 API。 |
| **mx-ymate-security** | Authentication and authorization module based on Sa-Token.<br/>基于 Sa-Token 的权限认证模块。 |
| **mx-ymate-sms** | SMS module supporting multiple providers (NetEase, Tencent, Alibaba, etc.).<br/>短信模块，支持网建、腾讯、阿里等服务商。 |
| **mx-ymate-upload** | File upload module supporting Local, Qiniu, MinIO, Tencent Cloud, and Alibaba Cloud.<br/>文件上传模块，支持本地、七牛、MinIO、腾讯云、阿里云。 |
| **mx-ymate-work-robot** | WeCom (WeChat Work) robot integration for message automation.<br/>微信企业号机器人模块。 |
| **Packaging** | One-click packaging into a WAR file for CLI deployment.<br/>一键打包为 WAR 包，可直接命令行部署。 |

---

## ⚙️ How to Use / 使用方式

Since the modules are not yet published to the Maven Central Repository,  
please install them locally before using:

由于尚未发布至 Maven 中央仓库，请先执行本地安装：

```bash
mvn clean install
```


## 📜 License / 开源协议

Licensed under the Apache License 2.0, free for commercial and personal use.
本项目基于 Apache License 2.0 开源，免费且可商用。


## 💬 Contribution & Feedback / 参与与反馈

Contributions, issues, and suggestions are welcome.
欢迎提交 Issue 或 PR，共同完善项目生态。

If this project helps you, please give it a ⭐ to support!
如果项目对你有帮助，请为它点亮一个 ⭐！
