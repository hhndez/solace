#!/bin/sh
RETVAL=0
VERSION=@version@
MASSIVE_HOME=@environment.app.home@

validateAppMode() {
	if [ $1 = "start" -o $1 = "stop" -o $1 = "smoke" ]
	then
		return 0
	fi
	return 1
}

#Shell Body
if [ $# -le 0 -o $# -gt 1 ]
then
	echo "Syntax Command: $0 App_Mode"
	echo "Where App_Mode is: [start | stop | smoke]"
	exit 1
fi

validateAppMode $1

if [ $? = 0 ]
then
	cd  $MASSIVE_HOME/$VERSION/bin
	case "$1" in
		start)
			sh startup.sh
			RETVAL=$?
			;;
		stop)
			sh shutdown.sh
			RETVAL=$?
			;;
		smoke)
		    sh smoke.sh
		    RETVAL=$?
		    ;;
	esac
	exit $RETVAL
else
	echo "Syntax Command: $0 App_Mode"
	echo "Where App_Mode is: [start | stop | smoke]"
	exit 1
fi