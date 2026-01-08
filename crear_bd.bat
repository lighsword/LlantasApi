@echo off
REM ================================================================
REM Script para ejecutar la creación de base de datos
REM ================================================================

echo ========================================
echo CREANDO BASE DE DATOS COMPLETA
echo ========================================
echo.

REM Configuración
set PGHOST=localhost
set PGPORT=5432
set PGDATABASE=BackendLlantas
set PGUSER=postgres
set PGPASSWORD=123456

echo Conectando a PostgreSQL...
echo.

REM Crear base de datos si no existe
psql -U %PGUSER% -c "CREATE DATABASE \"BackendLlantas\";" 2>nul

REM Ejecutar script
echo Ejecutando script de creación...
psql -U %PGUSER% -d %PGDATABASE% -f CREAR_BASE_DATOS_COMPLETA.sql

echo.
echo ========================================
echo PROCESO COMPLETADO
echo ========================================
echo.
echo Verifica que no haya errores arriba.
echo Si todo esta OK, puedes iniciar la aplicacion.
echo.

pause

