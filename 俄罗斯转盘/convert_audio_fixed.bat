@echo off
REM convert_audio_fixed.bat - 修复版音频转换工具
title FFmpeg音频转换修复版

REM ████████ 用户配置区域 ████████
set FFMPEG_PATH="D:\app\ffmpeg-7.1.1-full_build\bin\ffmpeg.exe"
set INPUT_DIR="C:\Users\43590\Desktop\javawork\gun\project-root\resources\sounds"
set OUTPUT_DIR="C:\Users\43590\Desktop\javawork\gun\project-root\resources\sounds_converted"
REM ████████████████████████████████

chcp 65001 > nul
setlocal enabledelayedexpansion

echo [INFO] 开始音频格式转换流程...

REM █ 验证FFmpeg路径
if not exist %FFMPEG_PATH% (
    echo.
    echo [ERROR] FFmpeg未找到，请检查路径是否正确：
    echo 当前路径：%FFMPEG_PATH%
    echo 建议检查：
    echo 1. 路径中的反斜杠是否缺失（如 binffmpeg.exe → bin\ffmpeg.exe）
    echo 2. 文件是否被误删
    pause
    exit /b 1
)

REM █ 创建输出目录
if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"

REM █ 批量转换
for %%F in ("%INPUT_DIR%\*.mp3" "%INPUT_DIR%\*.wav" "%INPUT_DIR%\*.ogg") do (
    echo [PROCESS] 正在转换：%%~nxF
    %FFMPEG_PATH% -y -i "%%F" -acodec pcm_s16le -ar 44100 -ac 2 "%OUTPUT_DIR%\%%~nF.wav"
    if !errorlevel! == 0 (
        echo [SUCCESS] 转换成功：%%~nF.wav
    ) else (
        echo [FAILED] 转换失败：%%~nxF
    )
)

echo [INFO] 转换完成！输出目录：%OUTPUT_DIR%
pause