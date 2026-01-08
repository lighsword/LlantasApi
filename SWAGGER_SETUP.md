# 🔧 Configuración de Swagger/OpenAPI

## ✅ El error 500 en `/v3/api-docs` ha sido corregido

### 🚀 Pasos para ver Swagger UI funcional:

#### 1. **Limpiar y recompilar**
```bash
cd "D:\project programation\Proyectos en Java\LlantasApi"
set JAVA_HOME=C:\Program Files\Java\jdk-21
mvnw.cmd clean compile
```

#### 2. **Iniciar la aplicación**
Opción A - Desde IntelliJ:
- Botón verde "Run" (▶)
- O presiona `Shift + F10`

Opción B - Desde terminal:
```bash
mvnw.cmd spring-boot:run
```

#### 3. **Esperar a que la app inicie**
Verás en los logs:
```
Se ha iniciado correctamente el sistema.
```

#### 4. **Acceder a Swagger UI**
Abre tu navegador en:
```
http://localhost:8081/swagger-ui.html
```

O la versión alternativa:
```
http://localhost:8081/swagger-ui/index.html
```

---

## 🔐 Autenticar en Swagger

### Paso 1: Registrar/Login
1. En Swagger, busca **`/api/auth/register`** o **`/api/auth/login`**
2. Click en "Try it out"
3. Ingresa credenciales (JSON):
```json
{
  "email": "admin@ejemplo.com",
  "nombre": "Admin",
  "password": "admin123",
  "rol": "ADMIN"
}
```
4. Click en "Execute"
5. **Copia el `accessToken`** de la respuesta

### Paso 2: Autorizar Swagger
1. Click en el botón **"Authorize"** 🔒 (arriba a la derecha en Swagger)
2. En el campo de texto, ingresa:
```
Bearer {tu_accessToken}
```
Ejemplo:
```
Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlamp...
```
3. Click en **"Authorize"**
4. Click en **"Close"**

Ahora todos los endpoints protegidos funcionarán.

---

## 🔧 Troubleshooting

### Si aún sale error 500 en `/v3/api-docs`:

**Opción 1: Reiniciar IntelliJ**
- Cierra completamente IntelliJ
- Borra la carpeta `.idea`
- Vuelve a abrir el proyecto

**Opción 2: Limpiar caché de Maven**
```bash
mvnw.cmd clean
rd /s /q .m2\repository
mvnw.cmd install
```

**Opción 3: Ver logs detallados**
```bash
mvnw.cmd spring-boot:run -X 2>&1 | head -100
```

---

## 📝 Endpoints de prueba sin autenticación

Estos endpoints NO necesitan token:
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/refresh`
- `GET /api/enums/roles`

---

## 🎯 Resultado esperado

✅ Swagger UI carga correctamente
✅ Puedes ver todos los endpoints
✅ Login/Register funcionan
✅ Otros endpoints responden con 401 sin token
✅ Con token Bearer autorizado, todos funcionan

Si esto no ocurre, verifica:
1. ¿Está la app corriendo en `http://localhost:8081`?
2. ¿Has copiado el token completo (incluyendo `Bearer `)?
3. ¿La app compila sin errores?

---

**Última actualización:** 8 enero 2026

