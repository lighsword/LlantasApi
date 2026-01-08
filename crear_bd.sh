#!/bin/bash
# ================================================================
# Script para ejecutar la creación de base de datos (Linux/Mac)
# ================================================================

echo "========================================"
echo "CREANDO BASE DE DATOS COMPLETA"
echo "========================================"
echo ""

# Configuración
export PGHOST=localhost
export PGPORT=5432
export PGDATABASE=BackendLlantas
export PGUSER=postgres
export PGPASSWORD=123456

echo "Conectando a PostgreSQL..."
echo ""

# Crear base de datos si no existe
psql -U $PGUSER -c "CREATE DATABASE \"BackendLlantas\";" 2>/dev/null

# Ejecutar script
echo "Ejecutando script de creación..."
psql -U $PGUSER -d $PGDATABASE -f CREAR_BASE_DATOS_COMPLETA.sql

echo ""
echo "========================================"
echo "PROCESO COMPLETADO"
echo "========================================"
echo ""
echo "Verifica que no haya errores arriba."
echo "Si todo está OK, puedes iniciar la aplicación."
echo ""

