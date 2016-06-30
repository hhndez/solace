#!/bin/sh



if [ "x$1" = "x" ]; then
        echo "Se debe de seleccionar el archivo a inspeccionar ..."
        exit -1
fi

if [ "x$2" = "x" ]; then
        echo "Se debe insertar la palabra de éxito ..."
        exit -1
fi


logFile=$1
successWord=$2
errorWord="Exception"
endWord="Killed"
timeToSleep=15
numIntentos=30


checkCharPhrase(){
        foundChar=`echo $1 | grep $2 `
        return isExistChar  $foundChar
        
}       

checkFilePhrase(){

        foundChar=`cat $1 | grep $2`
#       echo "foundchar $foundChar"
        isExistChar  $foundChar
}       


isExistChar(){
        #echo "isExistChar $1"
        
        if [ "x$1" = "x" ]; 
                then
                RETVAL=0
        else
                RETVAL=1
        fi
}


#empieza la validacion

times=0
while [ 1 ]; do
        if [ $times -gt $numIntentos ]; then 
                echo "Se sobrepaso el numero limite de intentos:.  $times"
tail -10 $logFile
                exit -1
        fi      
        
        checkFilePhrase $logFile $endWord
        #echo "RETVAL $RETVAL"
        if [ $RETVAL -eq 1 ]; 
                then 
                echo "Se detecto una baja del proceso ..."
                tail -10 $logFile
                exit -1 
        else    
                checkFilePhrase $logFile $errorWord
                #echo "RETVAL $RETVAL"
                if [ $RETVAL -eq 1 ]; 
                                then 
                                        echo "Se detecto un error ... "
                                        tail -10 $logFile
                                        exit -1
                else
                        checkFilePhrase $logFile $successWord
                        #echo "RETVAL $RETVAL"
                        if [ $RETVAL -eq 1 ]; 
                                then
                                        echo "se encontró $successWord"
                                        exit 0;
                        else
                                times=`echo "$times + 1" | bc`
                                echo "esperando $timeToSleep segundos, para el intento $times"
                                sleep $timeToSleep
                        fi
                fi
        fi
                
done    
