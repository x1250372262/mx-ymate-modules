#!/bin/sh
DIR=$(cd $(dirname $0) && pwd )
echo  $(dirname $0)
nohup java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED -jar  -Dymp.configFile=$DIR/conf/ymp-conf.properties  -Dymp.configHome=$DIR  sft-tej-manager.war --targetDir deployfile --cleanup --port 8082 &
ps -ef | grep sft-tej-manager