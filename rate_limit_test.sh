#!/bin/bash

# 目标URL
URL="http://localhost:8080/api/hello"

# 发送请求的次数
TOTAL_REQUESTS=110

# 成功请求计数
SUCCESS_COUNT=0

# 失败请求计数
FAIL_COUNT=0

# 发送请求
for ((i=1; i<=TOTAL_REQUESTS; i++)); do
    RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" $URL)
    if [ $RESPONSE -eq 200 ]; then
        SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
    elif [ $RESPONSE -eq 429 ]; then
        FAIL_COUNT=$((FAIL_COUNT + 1))
    fi
    echo "Request $i: HTTP $RESPONSE"
done

echo "Total Successful Requests: $SUCCESS_COUNT"
echo "Total Failed Requests (429 Too Many Requests): $FAIL_COUNT"

