# 🧩 mx-ymate-modules

> 🚀 A modular development toolkit based on **YMP (ymate-platform-v2)** for fast backend development.

---

## 📦 Modules Overview

| 模块名称 (Module) | 功能简介 (Description) |
|--------------------|-------------------------|
| **mx-ymate-clickhouse** | Encapsulates ClickHouse for high-performance data storage and analytics. |
| **mx-ymate-dev** |Core module with utility classes, response wrappers, and shared functions. |
| **mx-ymate-excel** |Excel import/export utilities. |
| **mx-ymate-manager-templates** | Admin panel templates designed for backend developers (non-Vue). |
| **mx-ymate-maven-plugin** | Code generator Maven plugin with customizable templates. |
| **mx-ymate-monitor** | System and server monitoring module. |
| **mx-ymate-mqtt** | MQTT module supporting pub/sub and Paho clients. |
| **mx-ymate-netty** |Netty-based communication with built-in heartbeat, server/client capable. |
| **mx-ymate-qwen** |LLM module based on Alibaba Qwen API. |
| **mx-ymate-redis** |Redis integration with simplified API. |
| **mx-ymate-security** | Authentication & authorization module using Sa-Token. |
| **mx-ymate-sms** | SMS module supporting multiple vendors (Tencent, Alibaba, etc.). |
| **mx-ymate-upload** |File upload module supporting local and multiple cloud providers. |
| **mx-ymate-work-robot** |WeCom (WeChat Work) robot integration module. |
| **打包文件 / Packaging** |One-click WAR packaging for CLI deployment. |

---


## ⚙️ How to Use

Since the modules are not yet published to Maven Central, please install them locally：  

```bash
mvn clean install