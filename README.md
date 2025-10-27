# 🧩 mx-ymate-modules

> 🚀 基于 **YMP（ymate-platform-v2）** 框架的模块化快速开发组件集 

---

## 📦 模块说明

| 模块名称 (Module) | 功能简介 (Description) |
|--------------------|-------------------------|
| **mx-ymate-clickhouse** | 封装 ClickHouse，用于海量数据的高性能存储与查询。|
| **mx-ymate-dev** | 核心模块，提供常用工具类、通用返回结果等基础功能。|
| **mx-ymate-excel** | 提供 Excel 导入导出功能。 |
| **mx-ymate-manager-templates** | 后台管理模板（非 Vue 实现）。|
| **mx-ymate-maven-plugin** | Maven 插件，可根据配置文件生成代码，模板可自定义。|
| **mx-ymate-monitor** | 系统与服务器监控模块。|
| **mx-ymate-mqtt** | MQTT 模块，支持发布/订阅，兼容 Paho 等客户端。 |
| **mx-ymate-netty** | Netty 通信模块，内置心跳检测，可当服务端或客户端使用。|
| **mx-ymate-qwen** | 基于阿里通义千问封装的 LLM 模块。|
| **mx-ymate-redis** | Redis 整合模块，封装常用操作 API。 |
| **mx-ymate-security** | 基于 Sa-Token 的权限认证模块。|
| **mx-ymate-sms** | 短信模块，支持网建、腾讯、阿里等服务商。 |
| **mx-ymate-upload** | 文件上传模块，支持本地、七牛、MinIO、腾讯云、阿里云。|
| **mx-ymate-work-robot** | 微信企业号机器人模块。|
| **打包文件 / Packaging** | 一键打包为 WAR 包，可直接命令行部署。|

---


## ⚙️ 使用方式

由于尚未发布至 Maven 中央仓库，请先执行本地安装：  

```bash
mvn clean install