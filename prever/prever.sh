#!/usr/bin/env bash

nohup java -cp ./libs/*:./conf io.prever.apps.monitor.WebMonitor > /dev/null 2>&1&
