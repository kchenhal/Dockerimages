#!/bin/bash

sleep 10

echo "waking up cmdhandler"
java -jar /geodeCmdHandler.jar "$@" 
