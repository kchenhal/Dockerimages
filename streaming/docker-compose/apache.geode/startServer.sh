#!/bin/bash

mkdir -p /data/$HOSTNAME

echo 'sleeping 15'
sleep 15

echo 'wakeup geode server'

gfsh start server --name=$HOSTNAME --dir=/data/$HOSTNAME/ "$@"

while true; do
    sleep 10
  done
done


