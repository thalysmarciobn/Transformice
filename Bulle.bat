@echo off
title Transformice
:start
java -Dfile.encoding=UTF-8 -XX:+UseConcMarkSweepGC -Xmx1G -cp config;./lib/* com.transformice.JBulle
pause