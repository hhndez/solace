#!/bin/sh

export JRE=@environment.jrePath@
export LLM_HOME=@environment.llmHome@
export LD_LIBRARY_PATH=$LLM_HOME/lib64
export MEM_PARAMS="@environment.app.memParams@"
export GC_PARAMS="@environment.app.gcParams@"
export SERVER_ADDRESS="@environment.address@"
export DEBUG_PORT="@environment.app.debugPort@"
export JMX_PORT="@environment.app.jmxPort@"
export JMX_PARAMS="-Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=$SERVER_ADDRESS"
export DEBUG_PARAMS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=$DEBUG_PORT"
export PROCESS_NAME="@environment.app.processName@"
export MASSIVE_HOME="@environment.app.home@"
export MASSIVE_LIB="$MASSIVE_HOME/@version@/lib"
export MASSIVE_CONFIG_PATH="$MASSIVE_HOME/@version@/etc"
export MASSIVE_CLASSPATH="$MASSIVE_HOME/@version@/etc"
export LOG_PATH="@environment.app.logPath@"
export ENCODING="@environment.app.encoding@"
for i in `ls -ltrh $MASSIVE_LIB | awk -F' ' '{print $9}'`
do
	MASSIVE_CLASSPATH=$MASSIVE_CLASSPATH:$MASSIVE_LIB/$i;
done
export MASSIVE_CLASSPATH=$MASSIVE_CLASSPATH