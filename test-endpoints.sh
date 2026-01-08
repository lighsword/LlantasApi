#!/bin/bash
# Script rápido para probar los endpoints
# Uso: bash test-endpoints.sh

API_URL="${1:-http://localhost:8081}"

echo "
╔════════════════════════════════════════════════════════════════╗
║     🧪 PRUEBA RÁPIDA DE ENDPOINTS DE CATEGORÍAS              ║
╚════════════════════════════════════════════════════════════════╝
"

echo "📌 API URL: $API_URL"
echo ""

# Función para hacer request con bonito formato
test_endpoint() {
    local method=$1
    local endpoint=$2
    local description=$3

    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "🔍 $description"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "Endpoint: $method $endpoint"
    echo ""

    if [ "$method" = "GET" ]; then
        curl -s -X GET "$API_URL$endpoint" \
            -H "Content-Type: application/json" \
            -H "Accept: application/json" | jq . 2>/dev/null || echo "⚠️  Error al procesar respuesta"
    fi

    echo ""
}

# Test 1: Categorías del enum
test_endpoint "GET" "/api/enums/categorias" "TEST 1: Obtener categorías del enum (RECOMENDADO)"

# Test 2: Todos los enums
test_endpoint "GET" "/api/enums" "TEST 2: Obtener todos los enums del sistema"

# Test 3: Extractar solo categorías de all enums
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔍 TEST 3: Extraer solo categorías de /api/enums"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Endpoint: GET /api/enums (extraer .categorias)"
echo ""
curl -s -X GET "$API_URL/api/enums" \
    -H "Content-Type: application/json" | jq '.categorias' 2>/dev/null || echo "⚠️ Error"
echo ""

# Test 4: Endpoint antiguo (BD)
test_endpoint "GET" "/api/categorias" "TEST 4: Categorías de la BD (Sistema antiguo)"

# Test 5: Swagger UI
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📚 TEST 5: Acceso a Swagger UI"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "Abre en tu navegador:"
echo "  🌐 $API_URL/swagger-ui.html"
echo ""

# Resumen
echo "
╔════════════════════════════════════════════════════════════════╗
║                   📊 RESUMEN DE PRUEBAS                       ║
╚════════════════════════════════════════════════════════════════╝

✅ Pruebas completadas

📌 Endpoints principales:
   • GET $API_URL/api/enums/categorias     (Array de strings)
   • GET $API_URL/api/categorias           (Objetos de BD)
   • GET $API_URL/api/enums                (Todos los enums)

📚 Documentación:
   • $API_URL/swagger-ui.html

💡 Próximo paso:
   Implementar en tu frontend el consumo de:
   GET /api/enums/categorias

"

