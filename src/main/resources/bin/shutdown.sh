#!/bin/sh

PROCESS_NAME="@environment.app.processName@"
KILLED=0

PIDS=`ps -fea | grep $PROCESS_NAME | grep -v grep | awk -F' ' '{print $2}'`
if [ -z "$PIDS" ]
then
    echo "Process with name [$PROCESS_NAME] was not found."
    exit 1
fi

echo "Finishing process with id: [$PIDS]"
date
kill -9 $PIDS > /dev/null 2>&1
KILLED=$?
exit $KILLED

