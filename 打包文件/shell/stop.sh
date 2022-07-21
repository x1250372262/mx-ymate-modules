#!/bin/sh

PID=`ps -ef | grep sft-tej-manager | awk -F' ' '{ if($10=="sft-tej-manager-1.0.0.war") print $2}'`

if [ -n "$PID" ]; then
    echo "sft-tej-manager running with pid $PID detected, killing it."
    echo $PID | xargs kill -5
else
    echo 'ignored stop signal since no sft-tej-manager process found.'
fi