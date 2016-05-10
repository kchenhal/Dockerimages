#!/bin/bash

mkdir -p /data/$HOSTNAME

echo 'sleeping 20s'
sleep 20

echo 'wakeup'

gfsh start server --name=$HOSTNAME --dir=/data/$HOSTNAME/ "$@"

while true; do
    sleep 10
  done
done


