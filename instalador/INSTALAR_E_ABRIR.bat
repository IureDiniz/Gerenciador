@echo off
cd /d "%~dp0"

start /wait "" "GerenciadorEstoque-Setup.exe"

set "APP_EXE="

if exist "%LOCALAPPDATA%\GerenciadorEstoque\GerenciadorEstoque.exe" (
    set "APP_EXE=%LOCALAPPDATA%\GerenciadorEstoque\GerenciadorEstoque.exe"
)

if not defined APP_EXE if exist "%ProgramFiles%\GerenciadorEstoque\GerenciadorEstoque.exe" (
    set "APP_EXE=%ProgramFiles%\GerenciadorEstoque\GerenciadorEstoque.exe"
)

if not defined APP_EXE if exist "%ProgramFiles(x86)%\GerenciadorEstoque\GerenciadorEstoque.exe" (
    set "APP_EXE=%ProgramFiles(x86)%\GerenciadorEstoque\GerenciadorEstoque.exe"
)

if defined APP_EXE (
    start "" "%APP_EXE%"
) else (
    echo Instalacao concluida, mas o executavel instalado nao foi encontrado automaticamente.
    echo Abra o GerenciadorEstoque pelo Menu Iniciar ou pelo atalho da Area de Trabalho.
    pause
)
