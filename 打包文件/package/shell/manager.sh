#!/bin/bash
# BASE_PATH: 当前脚本所在目录的绝对路径。
# shellcheck disable=SC2046
BASE_PATH="$(cd $(dirname "$0") || exit; pwd)"

# CLASSES_PATH: classes 目录的绝对路径，位于脚本所在目录的上一级。
# shellcheck disable=SC2046
CLASSES_PATH="$(cd $(dirname "${BASE_PATH}") || exit; pwd)/classes"

# RUN_DIR: 运行目录，设置为脚本所在目录的上一级目录。
# shellcheck disable=SC2046
RUN_DIR="$(cd $(dirname "${BASE_PATH}") || exit; pwd)"

#加载 env.sh 脚本文件（假设文件中定义了必要的环境变量，例如 APP_USER、APP_CONF_FILE 等）。
source "${BASE_PATH}"/env.sh

#检查当前登录用户是否与预期的启动用户一致。如果不一致，脚本会退出。这是为了确保服务运行在正确的权限环境中。
user="$(whoami)"
if [[ "${user}" != "${APP_USER}" ]];then
  echo "WARN: The current user '${user}' is inconsistent with the startup user '${APP_USER}'."
  echo "      Please confirm the logged in user or change the configuration file."
  exit 1
fi

start() {
   # 检查当前运行目录下是否已经有相关的 Java 进程在运行。
  # shellcheck disable=SC2009
  process="$(ps -ef|grep java|grep "${RUN_DIR}"|awk '{print $2}')"
  if [[ -n ${process} ]];then
    echo "WARN: $RUN_DIR has been started! PID: $process"
    exit 1
  fi
  # 如果配置文件不存在，发出警告并启动时使用默认配置。
  if [ ! -f "${APP_CONF_FILE}" ];then
    echo -e "WARN: Configuration file '${APP_CONF_FILE}' not found, starting with default configuration!"
  else
    echo "APP_CONF_FILE: ${APP_CONF_FILE}"
  fi
  echo "APP_STD_OUT: ${APP_STD_OUT}"
  echo "Starting ${RUN_DIR}"

# 确保日志目录存在，如果不存在则创建。
  if [ ! -d "${APP_LOGS}" ]; then
    mkdir "${APP_LOGS}"
  fi

# 启动 Java 应用程序，指定 classpath 和主类。
  nohup java -classpath "${CLASSES_PATH}" net.ymate.module.embed.Main --homeDir "${RUN_DIR}"/webapp &>"${APP_STD_OUT}"&
}

stop() {
   # 查找当前运行目录下的 Java 进程并终止。
  # shellcheck disable=SC2009
  process="$(ps -ef|grep java|grep "${RUN_DIR}"|awk '{print $2}')"
  if [[ -n ${process} ]];then
    echo "Stopping ${RUN_DIR}"
    kill "${process}"
  else
    echo "WARN: ${RUN_DIR} was not started!"
  fi
}

#先停止当前服务，等待 1 秒后重新启动服务。
restart() {
  stop
  sleep 1
  start
}

#提供脚本的用法说明。
usage() {
  echo "Usage: manager.sh <start|stop|restart|env>"
}

#输出当前的 Java 环境变量信息，方便调试和确认配置。
env() {
  echo "JDK_JAVA_OPTIONS: ${JDK_JAVA_OPTIONS}"
  echo "JAVA_TOOL_OPTIONS: ${JAVA_TOOL_OPTIONS}"
}

#检查输入参数是否正确（只接受一个参数）。
 #根据参数调用对应的函数（start、stop、restart、env）。
 #如果输入的参数不在上述范围内，显示脚本用法。
if [ ! $# == 1 ] ;then
    usage
    exit;
fi

action=$1

if [ "${action}" = "start" ] ;then
  start
elif [ "${action}" = "stop" ] ;then
  stop
elif [ "${action}" = "restart" ] ;then
  restart
elif [ "${action}" = "env" ] ;then
  env
else
  usage
fi
