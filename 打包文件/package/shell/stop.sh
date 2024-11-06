#!/bin/sh

PID=`ps -ef | grep sft-tej-manager | grep -v grep| awk ' { print $2}'`
echo $PID
if [ -n "$PID" ]; then
    echo "sft-tej-manager running with pid $PID detected, killing it."
    echo $PID | xargs kill -5
else
    echo 'ignored stop signal since no sft-tej-manager process found.'
fi