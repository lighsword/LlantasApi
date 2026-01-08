#!/usr/bin/env bash
# Build script para Render

echo "==> Iniciando build de LlantasApi..."

# Dar permisos de ejecución a mvnw
chmod +x mvnw

# Limpiar y compilar el proyecto
echo "==> Compilando con Maven..."
./mvnw clean package -DskipTests

# Verificar que el JAR se creó correctamente
if [ -f target/*.jar ]; then
    echo "==> Build exitoso! JAR creado."
else
    echo "==> Error: No se pudo crear el JAR"
    exit 1
fi

echo "==> Build completado exitosamente!"

