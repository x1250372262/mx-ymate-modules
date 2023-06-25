#!/bin/sh

PID=`ps -ef | grep 服务名 | awk -F' ' '{ if($10=="服务名.war") print $2}'`

if [ -n "$PID" ]; then
    echo "服务名 running with pid $PID detected, killing it."
    echo $PID | xargs kill -5
else
    echo 'ignored stop signal since no 服务名 process found.'
fi