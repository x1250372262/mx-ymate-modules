#!/bin/sh

nohup java -jar -Duser.home=/usr/local/java/servers/sft-tej-manager sft-tej-manager-1.0.0.war --port=8082 --cleanup >/dev/null 2>&1 &

ps -ef | grep sft-tej-manager