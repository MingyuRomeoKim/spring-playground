#!/bin/bash

# 실행할 모듈 목록과 해당 포트 정의 (순서 지정)
MODULES=("module-eureka" "module-webGateway" "module-api/module-api-auth" "module-api/module-api-article" "module-api/module-api-mail" "module-rabbitmq/module-rabbitmq-cunsumer")
PORTS=("8761" "8080" "18081" "18082" "18083" "25672")
MAX_WAIT=60  # 최대 대기 시간 (초)
WAIT_TIME=0

# 스크립트가 실행되는 디렉토리를 기준으로 설정
BASE_DIR=$(pwd)

# logs 및 pids 디렉토리 생성
mkdir -p logs
mkdir -p logs/module-api
mkdir -p logs/module-rabbitmq
mkdir -p pids
mkdir -p pids/module-api
mkdir -p pids/module-rabbitmq

chmod -R 755 logs
chmod -R 755 pids

# 0. 종료 스크립트 실행
echo "Stopping all modules..."
./stop-modules.sh

# 1. 전체 프로젝트 빌드
echo "Building the project..."
./gradlew clean build

if [ $? -ne 0 ]; then
    echo "Build failed. Exiting."
    exit 1
fi
echo "Build completed."

# 2. 모듈 실행
for i in "${!MODULES[@]}"; do
    MODULE="${MODULES[$i]}"
    PORT="${PORTS[$i]}"
    echo "Starting $MODULE on port $PORT..."
    MODULE_PATH="$BASE_DIR/$MODULE"
    JAR_NAME=$(ls $MODULE_PATH/build/libs/*.jar 2>/dev/null | grep -Ev '(plain|sources)' | head -n 1)

    if [ -z "$JAR_NAME" ]; then
        echo "Error: JAR file not found for $MODULE. Please check the build output."
        exit 1
    fi

    LOG_FILE="logs/$MODULE.log"

    chmod +x $JAR_NAME
    nohup java -jar "$JAR_NAME" --server.port="$PORT" > "$LOG_FILE" 2>&1 &

    echo $! > "pids/$MODULE.pid"
    echo "$MODULE started with PID $(cat "pids/"$MODULE.pid)"

    # 모듈이 시작될 때까지 로그 파일 모니터링
    echo "Waiting for $MODULE to start on port $PORT..."
    echo "Check the log file at $LOG_FILE"

    while ! grep -q "Started" "$LOG_FILE"; do
        sleep 1
        WAIT_TIME=$((WAIT_TIME + 1))
        if [ $WAIT_TIME -ge $MAX_WAIT ]; then
            echo "Timeout waiting for $MODULE to start."
            exit 1
        fi
    done
    echo "$MODULE is up and running."
done

echo "All modules have been started."
