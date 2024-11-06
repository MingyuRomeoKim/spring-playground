#!/bin/bash

MODULES=("module-eureka" "module-webGateway" "module-api/module-api-auth" "module-api/module-api-article")

# pids 디렉토리 경로
PID_DIR="pids"

# 프로세스 중지
for MODULE in "${MODULES[@]}"; do
    PID_FILE="$PID_DIR/$MODULE.pid"
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        echo "Stopping $MODULE with PID $PID..."
        kill "$PID"
        if [ $? -eq 0 ]; then
            echo "$MODULE stopped successfully."
            rm "$PID_FILE"
        else
            echo "Failed to stop $MODULE. It may have already been stopped."
            # 강제 종료를 원할 경우 아래 주석을 해제하세요.
            # kill -9 "$PID"
            # rm "$PID_FILE"
        fi
    else
        echo "PID file for $MODULE not found. Skipping."
    fi
done

echo "All modules have been stopped."
