hora #!/bin/bash

# Script de validación de endpoints de categorías
# Verifica que ambos endpoints funcionan correctamente

API_URL="http://localhost:8081"

echo "🔍 Validando Endpoints de Categorías..."
echo "========================================"
echo ""

# Test 1: Endpoint antiguo (BD)
echo "1️⃣ TEST: GET /api/categorias (Sistema Antiguo - BD)"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
curl -s -X GET "$API_URL/api/categorias" \
  -H "Content-Type: application/json" | jq . 2>/dev/null || echo "⚠️ Endpoint no disponible"

echo ""
echo ""

# Test 2: Endpoint nuevo (Enum)
echo "2️⃣ TEST: GET /api/enums/categorias (Sistema Nuevo - Enum)"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
curl -s -X GET "$API_URL/api/enums/categorias" \
  -H "Content-Type: application/json" | jq . 2>/dev/null || echo "⚠️ Endpoint no disponible"

echo ""
echo ""

# Test 3: Todos los enums
echo "3️⃣ TEST: GET /api/enums (Todos los Enums)"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
curl -s -X GET "$API_URL/api/enums" \
  -H "Content-Type: application/json" | jq '.categorias' 2>/dev/null || echo "⚠️ Endpoint no disponible"

echo ""
echo ""

# Test 4: Comparación de tamaños
echo "4️⃣ COMPARACIÓN: Tamaño de Respuestas"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
SIZE_ANTIGUO=$(curl -s -X GET "$API_URL/api/categorias" -H "Content-Type: application/json" | wc -c)
SIZE_NUEVO=$(curl -s -X GET "$API_URL/api/enums/categorias" -H "Content-Type: application/json" | wc -c)

echo "Tamaño /api/categorias (Antiguo): $SIZE_ANTIGUO bytes"
echo "Tamaño /api/enums/categorias (Nuevo): $SIZE_NUEVO bytes"

if [ $SIZE_ANTIGUO -gt 0 ] && [ $SIZE_NUEVO -gt 0 ]; then
  DIFERENCIA=$((SIZE_ANTIGUO / SIZE_NUEVO))
  echo "Diferencia: El endpoint nuevo es $DIFERENCIA veces más pequeño ⚡"
fi

echo ""
echo "✅ Validación completada"

