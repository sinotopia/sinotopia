#!/bin/bash
#
# springboot project startup script
#


SERVICE_PATH=$(cd `dirname $0`; pwd)
PARENT_PATH=$(cd `dirname $0`; cd ..;pwd)

MODULE_IMPL_NAME=${SERVICE_PATH##*/}
MODULE_NAME=${MODULE_IMPL_NAME%-impl*}
EXECUTE_JAR_NAME=$MODULE_IMPL_NAME.jar

EXECUTE_JAR_PATH=$SERVICE_PATH/$EXECUTE_JAR_NAME
DEFAULT_LOGGER_ROOT=/logs/dubbo-jars

JAVA_VERSION=1.7

getpid() {
    #pid=`pgrep -f "java.*$PROJECT"`
    pid=`ps -ef | grep "$MODULE_IMPL_NAME" | grep -v "$0" | grep -v "grep" |  awk '{print $2}'`
}

start() {
    getpid
    if [ -n "$pid" ]; then
        echo "$MODULE_NAME (pid $pid) is already running"
        exit 1
    fi

    if [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
        JAVA_CMD="$JAVA_HOME/bin/java"
    elif [[ -n "$JAVA" ]] && [[ -x "$JAVA" ]];  then
        JAVA_CMD="$JAVA"
    else
	JAVA_CMD=java
    fi

    if [[ "$JAVA_CMD" ]]; then
        version=$("$JAVA_CMD" -version 2>&1 | awk -F '"' '/version/ {print $2}')
        if [[ "$version" < $JAVA_VERSION ]]; then
            echo JAVA_CMD=$JAVA_CMD
	    echo Java version "$version" is less than required $JAVA_VERSION
            exit 1
        fi
    fi

    cd "$SERVICE_PATH"

    "$JAVA_CMD" -jar -Xdebug -Xnoagent -Xmx1024m -Xms1024m -XX:NewRatio=1 -XX:SurvivorRatio=8 -Dconfig.path=file:"$SERVICE_PATH"/";"file:"$PARENT_PATH"/common.properties -Dlogger.root="$DEFAULT_LOGGER_ROOT" -Dlogger.module="$MODULE_NAME" "$EXECUTE_JAR_PATH"  >> "$SERVICE_PATH"/out.nohup &

    echo -ne "Starting process"
    for i in {1..10}; do
        if ! [ -n "$pid" ]; then
            echo -ne "."
            sleep 1
            getpid
        fi
    done
    echo

    if [ -n "$pid" ]; then 
        status
    else 
        echo "Error during $MODULE_IMPL_NAME starting, see log for details."
fi
}

debug() {
    getpid
    if [ -n "$pid" ]; then
        echo "[DEBUG]$MODULE_NAME (pid $pid) is already running"
        exit 1
    fi

    if [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
        JAVA_CMD="$JAVA_HOME/bin/java"
    elif [[ -n "$JAVA" ]] && [[ -x "$JAVA" ]];  then
        JAVA_CMD="$JAVA"
    else
	JAVA_CMD=java
    fi

    if [[ "$JAVA_CMD" ]]; then
        version=$("$JAVA_CMD" -version 2>&1 | awk -F '"' '/version/ {print $2}')
        if [[ "$version" < $JAVA_VERSION ]]; then
            echo [DEBUG]JAVA_CMD=$JAVA_CMD
	    echo [DEBUG]Java version "$version" is less than required $JAVA_VERSION
            exit 1
        fi
    fi

    cd "$SERVICE_PATH"
	
    echo "[DEBUG]$MODULE_NAME start with deubg mode"
	
	DEBUG_PORT=`sed '/debug.port/!d;s/.*=//' *.properties | tr -d '\r'`
	
	if [ -z "$DEBUG_PORT" ]; then
        echo "[DEBUG]$MODULE_NAME debug.port not by set,please check all properties file in $SERVICE_PATH"
        exit 1
    fi
	
	echo "[DEBUG]the debug port:$DEBUG_PORT"
	
    "$JAVA_CMD" -jar -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=${DEBUG_PORT},server=y,suspend=n -Xmx512m -Xms512m -XX:NewRatio=1 -XX:SurvivorRatio=8 -Dconfig.path=file:"$SERVICE_PATH"/";"file:"$PARENT_PATH"/common.properties -Dlogger.root="$DEFAULT_LOGGER_ROOT" -Dlogger.module="$MODULE_NAME" "$EXECUTE_JAR_PATH"  >> "$SERVICE_PATH"/out.nohup &

    echo -ne "[DEBUG]Starting process"
    for i in {1..10}; do
        if ! [ -n "$pid" ]; then
            echo -ne "."
            sleep 1
            getpid
        fi
    done
    echo

    if [ -n "$pid" ]; then 
        status
    else 
        echo "[DEBUG]Error during $MODULE_IMPL_NAME starting, see log for details."
    fi
}

stop() {
    status
    if [ -n "$pid" ]; then
        echo -ne "Stopping process"
        kill $pid
        res=$?
        for i in {1..10}; do
            if [ -n "$pid" ]; then
                echo -ne "."
                sleep 1
                getpid
            fi
        done
        echo
        if ! [ -n "$pid" ]; then 
	    echo "$MODULE_IMPL_NAME has been successfully stopped."
        else 
	    echo "Error during $MODULE_IMPL_NAME stopping... $res"
        fi
    fi
}

status() {
    getpid
    if [ -n "$pid" ]; then 
        echo "$MODULE_IMPL_NAME (pid $pid) is running..."
    else 
        echo "$MODULE_IMPL_NAME is NOT running"
    fi
}

case "$1" in
    start)
        start
        ;;
	debug)
        debug
        ;;
    stop)
        stop
        ;;
    status)
        status
        ;;
    restart)
        stop
        start
        ;;
    redebug)
	stop
	debug
	;;
    *)
        echo $"Usage: $0 {start|stop|restart|debug|redebug|status}"
        exit 1
esac

exit 0