#!/bin/bash
/opt/bitnami/spark/bin/spark-submit \
    --deploy-mode "client" \
    --master "spark://spark-master:7077" \
    --executor-cores 4 \
    --executor-memory 2G \
    --num-executors 1 \
    --class "MainApp" \
    "target/scala-2.12/wordcount_2.12-0.1.jar" \
