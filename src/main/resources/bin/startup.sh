#!/bin/sh
echo '############################################################'
echo 'Initializing Massive Trading operations...'
echo '############################################################'

. ./setenv.sh

mkdir -p $LOG_PATH
$JRE -Dfile.encoding=$ENCODING -D$PROCESS_NAME $MEM_PARAMS $GC_PARAMS $JMX_PARAMS $DEBUG_PARAMS -cp $MASSIVE_CLASSPATH @mainClassName@> $LOG_PATH/massive-trade.out 2> $LOG_PATH/massive-trade.err &

sleep 10
PID=`ps -fea | grep $PROCESS_NAME | grep -v grep | awk '{print $2}'`

if [ -z "$PID" ]
then
	echo "Massive Trading is down. Check out log files on $LOG_PATH"
	exit 1
else
	echo "Massive Trading is up and running. PID = [$PID]."
	exit 0
fi
