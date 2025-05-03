@echo off
cd /d "C:\Users\43590\Desktop\javawork\gun\project-root"
javac -d out -encoding UTF-8 src\AudioFormatChecker.java
java -cp out AudioFormatChecker
pause