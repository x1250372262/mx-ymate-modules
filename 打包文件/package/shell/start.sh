#!/bin/sh
DIR=$(cd $(dirname $0) && pwd )
echo  $(dirname $0)
nohup java  -jar  -Dymp.configFile=$DIR/conf/ymp-conf.properties  -Dymp.configHome=$DIR  sft-tej-manager.war --targetDir deployfile --cleanup --port 8082 &
ps -ef | grep sft-tej-manager