#!/bin/sh
nohup java -jar 服务名.war --targetDir 部署位置 --port 端口 &
ps -ef | grep 服务名