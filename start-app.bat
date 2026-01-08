@echo off
REM ========================================
REM Script para iniciar LlantasAPI
REM ========================================

echo.
echo ============================================
echo   🚀 INICIANDO LLANTAS API
echo ============================================
echo.

REM Establecer JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk-21
echo ✅ JAVA_HOME configurado: %JAVA_HOME%

REM Ir a la carpeta del proyecto
cd /d "D:\project programation\Proyectos en Java\LlantasApi"
echo ✅ Ubicación: %cd%

REM Limpiar (opcional)
echo.
echo 🧹 Limpiando compilaciones anteriores...
call mvnw.cmd clean > nul 2>&1

REM Compilar
echo 📦 Compilando...
call mvnw.cmd compile
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error en compilación
    pause
    exit /b 1
)
echo ✅ Compilación exitosa

REM Iniciar
echo.
echo 🚀 Iniciando Spring Boot...
echo.
call mvnw.cmd spring-boot:run

pause

