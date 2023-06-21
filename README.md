# mx-ymate-modules

主要用于快速开发ymp项目，具体说明如下

- [mx-ymate-clickhouse](mx-ymate-clickhouse)  封装clickhouse 用于海量数据存储；
- [mx-ymate-dev](mx-ymate-dev)  核心模块，其他模块都依赖它。提供一些常用工具类，通用返回结果等等；
- [mx-ymate-excel](mx-ymate-excel) 整合excel 提供基本的导入导出功能
- [mx-ymate-manager-templates](mx-ymate-manager-templates) 后台管理页面模板 不是vue 更符合后端开发人员使用。
- [mx-ymate-netty](mx-ymate-netty) netty模块 内置心跳检测，可同时当服务端和客户端.
- [mx-ymate-redis](mx-ymate-redis) 整合redis  提供便利的api操作。
- [mx-ymate-satoken](mx-ymate-satoken) 整合sa-token
- [mx-ymate-security](mx-ymate-security) 基于sa-token 权限验证 需要数据库的支持，支持扩展自己的用户体系。
- [mx-ymate-serv](mx-ymate-serv) ymp内置服务模块，可同时当服务端和客户端.
- [mx-ymate-tomcat](mx-ymate-tomcat) 基于ymp框架的tomcat模块，支持直接main方法启动
- [mx-ymate-undertow](mx-ymate-undertow) undertow。支持随机端口
- [mx-ymate-upload](mx-ymate-upload) 上传文件模块，支持本地、七牛、minio、腾讯、阿里。也可以自己扩展。
- [打包文件](打包文件) 直接打成war包 命令行直接部署。









## 注
以上模块由于未发布到maven中央仓库，所以需要自己本地安装一下。测试
