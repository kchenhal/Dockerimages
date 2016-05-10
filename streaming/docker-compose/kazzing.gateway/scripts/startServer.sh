#!/bin/bash

echo 'sleeping 20s so that the geode locator can start up'
sleep 20

echo 'wakeup'

gateway.start
