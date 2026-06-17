#!/bin/bash

for i in $(seq 1 100); do
    echo "Run $i of 100..."
    ./gradlew test --rerun-tasks
    if [ $? -ne 0 ]; then
        echo "Failure on run $i"
        exit 1
    fi
done

echo "All 100 runs passed"
