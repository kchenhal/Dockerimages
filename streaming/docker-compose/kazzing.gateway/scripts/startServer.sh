#!/bin/bash

echo 'sleeping 20s so that the geode locator can start up'
sleep 20

IPADDRESS=$(ip addr show eth0 | grep "inet\b" | awk '{print $2}' | cut -d/ -f1)
echo $IPADDRESS

GATEWAY_OPTS="$GATEWAY_OPTS -Dgateway.hostname=$IPADDRESS"
echo $GATEWAY_OPTS
echo 'wakeup gateway'

gateway.start
