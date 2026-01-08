# Script para preparar el proyecto para despliegue en Render

Write-Host "==> Preparando proyecto para Render..." -ForegroundColor Green

# Verificar si estamos en un repositorio Git
if (-not (Test-Path ".git")) {
    Write-Host "ERROR: No estas en un repositorio Git" -ForegroundColor Red
    Write-Host "Inicializa Git primero con: git init" -ForegroundColor Yellow
    exit 1
}

# Dar permisos de ejecución a los scripts de bash en Git
Write-Host "==> Configurando permisos de ejecucion para scripts..." -ForegroundColor Cyan
git update-index --chmod=+x build.sh
git update-index --chmod=+x start.sh
git update-index --chmod=+x mvnw

# Verificar que los archivos necesarios existen
$archivosRequeridos = @(
    "render.yaml",
    "build.sh",
    "start.sh",
    "system.properties",
    "src/main/resources/application-prod.properties",
    "pom.xml"
)

Write-Host "==> Verificando archivos necesarios..." -ForegroundColor Cyan
$todoOk = $true
foreach ($archivo in $archivosRequeridos) {
    if (Test-Path $archivo) {
        Write-Host "  OK $archivo" -ForegroundColor Green
    } else {
        Write-Host "  FALTA $archivo" -ForegroundColor Red
        $todoOk = $false
    }
}

if (-not $todoOk) {
    Write-Host "`nERROR: Faltan archivos necesarios para el despliegue" -ForegroundColor Red
    exit 1
}

# Mostrar estado de Git
Write-Host "`n==> Estado del repositorio:" -ForegroundColor Cyan
git status --short

# Preguntar si desea commitear
Write-Host "`n==> Deseas commitear estos cambios? (S/N): " -ForegroundColor Yellow -NoNewline
$respuesta = Read-Host

if ($respuesta -eq "S" -or $respuesta -eq "s") {
    Write-Host "==> Agregando archivos al stage..." -ForegroundColor Cyan
    git add render.yaml build.sh start.sh system.properties
    git add src/main/resources/application-prod.properties
    git add pom.xml README.md README_DEPLOY.md

    Write-Host "==> Commiteando cambios..." -ForegroundColor Cyan
    git commit -m "Configure Render deployment with Java/Maven setup"

    Write-Host "`nCambios commiteados exitosamente!" -ForegroundColor Green

    Write-Host "`n==> Deseas hacer push al repositorio remoto? (S/N): " -ForegroundColor Yellow -NoNewline
    $respuestaPush = Read-Host

    if ($respuestaPush -eq "S" -or $respuestaPush -eq "s") {
        Write-Host "==> Haciendo push..." -ForegroundColor Cyan
        git push
        Write-Host "`nPush exitoso!" -ForegroundColor Green
    }
}

Write-Host "`n==> Siguiente paso:" -ForegroundColor Cyan
Write-Host "1. Ve a https://dashboard.render.com" -ForegroundColor White
Write-Host "2. Click en 'New +' -> 'Blueprint'" -ForegroundColor White
Write-Host "3. Conecta tu repositorio de GitHub" -ForegroundColor White
Write-Host "4. Render detectara automaticamente render.yaml" -ForegroundColor White
Write-Host "5. Click en 'Apply' para crear los servicios" -ForegroundColor White

Write-Host "`n==> Para mas informacion, consulta README_DEPLOY.md" -ForegroundColor Cyan
Write-Host "`nProyecto listo para Render!" -ForegroundColor Green

