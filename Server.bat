@echo off
title Transformice
:start
java -Dfile.encoding=UTF-8 -XX:+UseConcMarkSweepGC -Xmx512M -cp config;./lib/* com.transformice.JServer
pause