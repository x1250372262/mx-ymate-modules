# mx-ymate-modules

主要用于快速开发ymp项目，具体说明如下

- [mx-ymate-clickhouse](mx-ymate-clickhouse)  封装clickhouse 用于海量数据存储；
- [mx-ymate-dev](mx-ymate-dev)  核心模块，其他模块都依赖它。提供一些常用工具类，通用返回结果等等；
- [mx-ymate-excel](mx-ymate-excel) 整合excel 提供基本的导入导出功能
- [mx-ymate-manager-templates](mx-ymate-manager-templates) 后台管理页面模板 不是vue 更符合后端开发人员使用。
- [mx-ymate-maven-plugin](mx-ymate-maven-plugin) 代码生成器maven插件 根据配置文件生成代码，代码模板可自定义
- [mx-ymate-monitor](mx-ymate-monitor) 服务器监控模块
- [mx-ymate-mqtt](mx-ymate-mqtt) mqtt模块 mqtt服务发布订阅话题 支持paho等.
- [mx-ymate-netty](mx-ymate-netty) netty模块 内置心跳检测，可同时当服务端和客户端.
- [mx-ymate-qwen](mx-ymate-qwen) 基于阿里通义千问封装的llm大模型
- [mx-ymate-redis](mx-ymate-redis) 整合redis  提供便利的api操作。
- [mx-ymate-security](mx-ymate-security) 基于sa-token 权限验证 需要数据库的支持，支持扩展自己的用户体系。
- [mx-ymate-sms](mx-ymate-sms) 短信模块，支持网建、腾讯、阿里。也可以自己扩展。
- [mx-ymate-upload](mx-ymate-upload) 上传文件模块，支持本地、七牛、minio、腾讯、阿里。也可以自己扩展。
- [mx-ymate-work-robot](mx-ymate-work-robot) 微信企业号机器人
- [打包文件](打包文件) 直接打成war包 命令行直接部署。









## 注
以上模块由于未发布到maven中央仓库，所以需要自己本地安装一下。测试
