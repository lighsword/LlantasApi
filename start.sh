#!/usr/bin/env bash
# Start script para Render

echo "==> Iniciando LlantasApi..."

# Encontrar el archivo JAR
JAR_FILE=$(find target -name "*.jar" -not -name "*-original.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "==> Error: No se encontró el archivo JAR"
    exit 1
fi

echo "==> Ejecutando: $JAR_FILE"

# Configurar el puerto desde la variable de entorno de Render
export SERVER_PORT=${PORT:-8080}

# Ejecutar la aplicación
java -Dserver.port=$SERVER_PORT \
     -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} \
     $JAVA_TOOL_OPTIONS \
     -jar "$JAR_FILE"

