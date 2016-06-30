#!/bin/sh
echo '############################################################'
echo 'Massive Trading ops. smoke test...'
echo '############################################################'
. ./setenv.sh
PLACHEHOLDER_MATCHER="@.*@"
exitIfEmpty() {
	if [ -z "$1" ]
		then
		echo "Error, la variable $2 esta vacia."
		exit 1
	fi
}

exitIfPathDoesNotExist() {
	exitIfEmpty $1 $2

	ls $1 > /dev/null
	if [ $? -ne 0 ]
		then
		echo "Error, $2 no se encuentra instalado en la ruta '$1'."
		exit 1
	fi

}

exitIfPlaceHolderHasNotBeenReplaced() {
	echo $1 | grep $PLACHEHOLDER_MATCHER
	#El comando grep devuelve 0 si la regex empata en el contenido, lo cual indica que no se sustituyo el placeholder.
	if [ $? -eq 0 ]
		then
		echo "Error $?, El placeholder $2($1) no fue sustituido."
		exit 1
	fi
}

exitIfPlaceHoldersHaveNotBeenReplaced() {
	FILE=$1
	grep $PLACHEHOLDER_MATCHER $FILE
	if [ $? -eq 0 ]
		then
		echo "Error, Aún existen placeholders en el archivo $FILE."
		exit 1
	fi
}

exitIfDirCannotBeCreated() {
	mkdir -p $1

	if [ $? -ne 0 ]
		then
		echo "Error, el directorio de logs '$1' no ha podido ser creado."
		exit 1
	fi
}

exitIfPlaceHolderHasNotBeenReplaced $JRE "JRE"
exitIfPlaceHolderHasNotBeenReplaced $LLM_HOME "LLM_HOME"
exitIfPlaceHolderHasNotBeenReplaced $MEM_PARAMS "MEM_PARAMS"
exitIfPlaceHolderHasNotBeenReplaced $GC_PARAMS "GC_PARAMS"
exitIfPlaceHolderHasNotBeenReplaced $SERVER_ADDRESS "SERVER_ADDRESS"
exitIfPlaceHolderHasNotBeenReplaced $DEBUG_PORT "DEBUG_PORT"
exitIfPlaceHolderHasNotBeenReplaced $JMX_PORT "JMX_PORT"
exitIfPlaceHolderHasNotBeenReplaced $PROCESS_NAME "PROCESS_NAME"
exitIfPlaceHolderHasNotBeenReplaced $MASSIVE_HOME "MASSIVE_HOME"
exitIfPlaceHolderHasNotBeenReplaced $MASSIVE_LIB "MASSIVE_LIB"
exitIfPlaceHolderHasNotBeenReplaced $MASSIVE_CLASSPATH "MASSIVE_CLASSPATH"
exitIfPlaceHolderHasNotBeenReplaced $LOG_PATH "LOG_PATH"
exitIfPlaceHolderHasNotBeenReplaced $ENCODING "ENCODING"

exitIfPlaceHoldersHaveNotBeenReplaced $MASSIVE_CONFIG_PATH/cfpMassive.xml
exitIfPlaceHoldersHaveNotBeenReplaced $MASSIVE_CONFIG_PATH/log4j.xml
exitIfPlaceHoldersHaveNotBeenReplaced $MASSIVE_CONFIG_PATH/log4j2.xml
exitIfPlaceHoldersHaveNotBeenReplaced $MASSIVE_CONFIG_PATH/massiveTradeContext.xml
exitIfPlaceHoldersHaveNotBeenReplaced $MASSIVE_CONFIG_PATH/oracleDataBaseContext.xml


exitIfEmpty $LOG_PATH "LOG_PATH"
exitIfDirCannotBeCreated $LOG_PATH

exitIfPathDoesNotExist $JRE "Java"
exitIfPathDoesNotExist $LLM_HOME "LLM"

$JRE -cp $MASSIVE_CLASSPATH com.bursatec.massive.MassChingonSmokeTest
if [ $? -ne 0 ]
	then
	echo "Error, la prueba de conectividad basica ha fallado."
	exit 1
fi

echo "La prueba smoke fue exitosa!"
exit 0
