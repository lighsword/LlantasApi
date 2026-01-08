# 🔒 Solución: Error 500 en Swagger `/v3/api-docs`

## 📋 Problema Original
```
Failed to load API definition. Errors Hide
Fetch error response status is 500 /v3/api-docs
```

---

## ✅ Solución Aplicada

### 1. **Simplificación de SwaggerConfig.java**
- ❌ Eliminada: Descripción markdown gigante (con miles de caracteres)
- ✅ Agregada: Descripción simple y concisa
- ✅ Agregado: Try-catch para manejar errores en la generación de OpenAPI

**Cambios:**
```java
// ANTES - Causaba problemas
.description("""
    # 🚀 API REST de Nivel Empresarial
    [Texto de 2000+ líneas...]
    """)

// DESPUÉS - Funciona correctamente
.description("API REST de nivel empresarial para gestión integral de llantas...")
```

### 2. **Compilación Limpia**
```bash
mvnw.cmd clean compile
```

### 3. **Reinicio de la Aplicación**
```bash
mvnw.cmd spring-boot:run
```

---

## 🚀 Cómo Iniciar Ahora

### **Opción A: Usando el Script (Recomendado)**
Simplemente ejecuta:
```
D:\project programation\Proyectos en Java\LlantasApi\start-app.bat
```

### **Opción B: Desde IntelliJ**
1. Presiona `Shift + F10` (Run)
2. O click en el botón verde ▶

### **Opción C: Desde Terminal**
```bash
cd "D:\project programation\Proyectos en Java\LlantasApi"
set JAVA_HOME=C:\Program Files\Java\jdk-21
mvnw.cmd spring-boot:run
```

---

## 🔍 Verificar que Funciona

### **1. Esperar el mensaje de inicio:**
```
Se ha iniciado correctamente el sistema.
```

### **2. Abrir Swagger UI:**
```
http://localhost:8081/swagger-ui.html
```

Deberías ver:
- ✅ Título: "🛞 LlantasAPI - Sistema de Gestión de Llantas"
- ✅ Lista de endpoints en la izquierda
- ✅ Botón "Authorize" 🔒 arriba a la derecha
- ✅ Sin errores rojos

### **3. Probar un endpoint sin autenticación:**
```
GET /api/enums/roles
```

Click en "Try it out" → "Execute"
Debe retornar los roles disponibles.

---

## 🔐 Obtener Access Token

### **Paso 1: Register/Login**
1. Busca `POST /api/auth/login` en Swagger
2. Click en "Try it out"
3. Ingresa (en JSON):
```json
{
  "email": "admin@llantasapi.com",
  "password": "admin123"
}
```
4. Click "Execute"
5. **Copia el `accessToken`** de la respuesta

### **Paso 2: Autorizar Swagger**
1. Click en botón **"Authorize"** 🔒
2. Ingresa en el campo:
```
Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsbGFudGFzYXBpLmNvbSI...
```
3. Click "Authorize"
4. Click "Close"

Ahora todos los endpoints funcionarán.

---

## 🛠️ Si Aún No Funciona

### **Paso 1: Verifica que la app esté corriendo**
- ¿Ves el mensaje "Se ha iniciado correctamente el sistema."?
- ¿Puerto 8081 está disponible?

Intenta en el navegador:
```
http://localhost:8081/actuator/health
```

Debe responder `{"status":"UP"}` en JSON.

### **Paso 2: Limpia completamente**
```bash
mvnw.cmd clean
del /s /q target
mvnw.cmd compile
```

### **Paso 3: Reinicia IntelliJ**
Si usas IntelliJ:
1. File → Invalidate Caches
2. Cierra y reabre el proyecto

### **Paso 4: Verifica los logs de error**
En la consola de IntelliJ, busca:
```
ERROR
Exception
Failed
```

Si encuentras algo, compartir el error exacto.

---

## 📊 Checklist Final

- [ ] ¿Compiló sin errores? (`BUILD SUCCESS`)
- [ ] ¿La app inició? (`Se ha iniciado correctamente`)
- [ ] ¿Swagger UI carga? (`http://localhost:8081/swagger-ui.html`)
- [ ] ¿Ves los endpoints listados?
- [ ] ¿El login funcionó y obtuviste token?
- [ ] ¿Pudiste autorizar el token en Swagger?
- [ ] ¿Otros endpoints responden correctamente?

Si todos son ✅, **Swagger está funcionando correctamente.**

---

## 📝 Archivos Modificados

- `src/main/java/.../Config/SwaggerConfig.java` - Simplificado
- `start-app.bat` - Script para iniciar fácilmente
- `SWAGGER_SETUP.md` - Guía de configuración

---

**¡Listo! Ahora deberías ver Swagger funcionando correctamente** 🎉

Cualquier problema adicional, reporta el error exacto que aparezca en los logs.

