@echo off
chcp 65001 > nul
set JAVA_OPTS=-Dfile.encoding=UTF-8 -Dsun.java2d.d3d=false

javac -encoding UTF-8 SnakeGame.java && java %JAVA_OPTS% SnakeGame
pause