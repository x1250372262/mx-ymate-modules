#!/bin/bash

# 如果环境变量没有 APP_CONF_PATH，则设置为 /opt/conf，否则使用已有的值
export APP_CONF_PATH=${APP_CONF_PATH:-/opt/conf}  # 示例：APP_CONF_PATH=/opt/conf

# 如果环境变量没有 APP_DATA_PATH，则设置为 /data，否则使用已有的值
export APP_DATA_PATH=${APP_DATA_PATH:-/data}  # 示例：APP_DATA_PATH=/data

# 如果环境变量没有 APP_RUN_ENV，则设置为空，否则使用已有的值
export APP_RUN_ENV=${APP_RUN_ENV:-}  # 示例：APP_RUN_ENV=

# 如果环境变量没有 APP_USER，则设置为 deploy，否则使用已有的值
export APP_USER=${APP_USER:-deploy}  # 示例：APP_USER=deploy

# 如果环境变量没有 APP_LOGS，则设置为当前用户的家目录 (~)，否则使用已有的值
export APP_LOGS=${APP_LOGS:-~}  # 示例：APP_LOGS=/home/user

# 如果环境变量没有 MEM，则设置为空，否则使用已有的值
export MEM=${MEM:-}  # 示例：MEM=

# 获取脚本所在目录的绝对路径，存储在 BASE_PATH 中
BASE_PATH="$(cd $(dirname "$0") || exit; pwd)"  # 示例：BASE_PATH=/mnt/java/tej/app

# 获取脚本所在目录的上一级目录路径，存储在 DIR_NAME 中
DIR_NAME="$(dirname "${BASE_PATH}")"  # 示例：DIR_NAME=/mnt/java/tej

# 获取脚本所在目录的上一级目录名，存储在 PACKAGE_NAME 中
PACKAGE_NAME="$(basename "${DIR_NAME}")"  # 示例：PACKAGE_NAME=tej

# 使用 sed 提取 PACKAGE_NAME 中的版本号（形如 x.x.x）并存储到 APP_VERSION 中
APP_VERSION=$(echo "${PACKAGE_NAME}" | sed 's/.*\([0-9]\.[0-9]\.[0-9]\).*/\1/g')  # 示例：APP_VERSION=1.0.0

# 去掉 PACKAGE_NAME 中的版本号部分，存储为 APP_NAME
export APP_NAME=${PACKAGE_NAME%-"${APP_VERSION}"*}  # 示例：APP_NAME=tej

# 如果存在 /proc/stat 文件，则计算 CPU 核心数，并存储在 CPU_COUNT 中
if [ -f "/proc/stat" ]; then
  export CPU_COUNT="$(grep -c 'cpu[0-9][0-9]*' /proc/stat)"  # 示例：CPU_COUNT=4
fi

# 如果 APP_RUN_ENV 被设置，则构造配置文件路径
if [[ -n ${APP_RUN_ENV} ]]; then
  export APP_CONF_FILE="${APP_CONF_PATH}/${APP_NAME}/ymp-conf_${APP_RUN_ENV}.properties"  # 示例：APP_CONF_FILE=/opt/conf/tej/ymp-conf_dev.properties
else
  export APP_CONF_FILE="${APP_CONF_PATH}/${APP_NAME}/ymp-conf.properties"  # 示例：APP_CONF_FILE=/opt/conf/tej/ymp-conf.properties
fi

# 设置标准输出日志文件路径
export APP_STD_OUT="${APP_LOGS}/${APP_NAME}_std_out.log"  # 示例：APP_STD_OUT=/home/user/tej_std_out.log

# 设置初始的 JDK_JAVA_OPTIONS
JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} -server"  # 示例：JDK_JAVA_OPTIONS=-server

# 如果 MEM 被设置，则追加到 JDK_JAVA_OPTIONS，否则根据系统内存设置默认值
if [[ -n ${MEM} ]]; then
  JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS}  ${MEM}"  # 示例：JDK_JAVA_OPTIONS=-server -Xmx2g
else
  if [ -f "/proc/meminfo" ]; then
    memTotal=$(cat /proc/meminfo | grep MemTotal | awk '{printf "%d", $2/1024*0.75 }')  # 计算内存的 75%
    if [ "${memTotal}" -gt 60000 ]; then
      JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} -Xms4g -Xmx4g"
      JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} -Xmn2g"  # 示例：JDK_JAVA_OPTIONS=-server -Xms4g -Xmx4g -Xmn2g
    else
      JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} -Xms2g -Xmx2g"
      JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} -Xmn1g"  # 示例：JDK_JAVA_OPTIONS=-server -Xms2g -Xmx2g -Xmn1g
    fi
  fi
fi

# 配置 JVM 的元空间和直接内存参数
JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m"  # 示例：MetaspaceSize=256m
JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} -XX:MaxDirectMemorySize=1g"  # 示例：MaxDirectMemorySize=1g
JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} -XX:SurvivorRatio=10"  # 示例：SurvivorRatio=10

# 如果 CPU_COUNT 被设置，则设置并行 GC 线程数
if [[ -n ${CPU_COUNT} ]]; then
  JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} -XX:ParallelGCThreads=${CPU_COUNT}"  # 示例：ParallelGCThreads=4
fi

# 配置 JVM 在 OOM 时生成堆转储文件
JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${APP_LOGS}/${APP_NAME}_java.hprof"  # 示例：HeapDumpPath=/home/user/tej_java.hprof

# 配置 JVM 模块的开放访问选项
JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} --add-opens=java.base/java.lang=ALL-UNNAMED"
JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} --add-opens=java.base/java.io=ALL-UNNAMED"
JDK_JAVA_OPTIONS="${JDK_JAVA_OPTIONS} --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED"
export JDK_JAVA_OPTIONS  # 导出 JDK_JAVA_OPTIONS 环境变量

# 配置 JAVA_TOOL_OPTIONS 环境变量
JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS} -Djava.net.preferIPv4Stack=true"  # 示例：preferIPv4Stack=true
JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS} -Djava.security.egd=file:/dev/./urandom"  # 示例：egd=file:/dev/./urandom
JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS} -Djava.awt.headless=true"  # 示例：headless=true
JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS} -Dsun.net.client.defaultConnectTimeout=10000"  # 示例：defaultConnectTimeout=10000
JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS} -Dsun.net.client.defaultReadTimeout=30000"  # 示例：defaultReadTimeout=30000
JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS} -Dfile.encoding=UTF-8"  # 示例：file.encoding=UTF-8
JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS} -Dymp.configHome=${APP_CONF_PATH}/${APP_NAME}"  # 示例：configHome=/opt/conf/tej
JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS} -Dymp.configFile=${APP_CONF_FILE}"  # 示例：configFile=/opt/conf/tej/ymp-conf.properties
export JAVA_TOOL_OPTIONS  # 导出 JAVA_TOOL_OPTIONS 环境变量
