@echo off
cd /d "%~dp0"
rmdir /s /q out 2>nul
javac -d out -encoding UTF-8 src/*.java
java -cp "out;resources" MainApplication
pause