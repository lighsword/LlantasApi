# API de Negocio de Llantas

API REST desarrollada en Spring Boot para la gestión de categorías de productos en un negocio de llantas.

## Características

- ✅ Arquitectura RESTful
- ✅ Integración con base de datos PostgreSQL
- ✅ Validación de datos
- ✅ Manejo de excepciones global
- ✅ Documentación automática con OpenAPI (Swagger)
- ✅ Control de acceso a recursos
- ✅ Soporte para operaciones CRUD completas

## Tecnologías utilizadas

- **Java 23**
- **Spring Boot 3.4.2**
- **Spring Web MVC**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **OpenAPI (Swagger)**
- **Maven**

## Requisitos previos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

- Java 23 o superior
- Maven 3.6 o superior
- PostgreSQL
- Git

## Instalación

1. Clona el repositorio:
```bash
git clone <URL_DEL_REPOSITORIO>
cd LlantasApi
```

2. Configura la base de datos PostgreSQL:
```sql
CREATE DATABASE llantasdb;
-- Usuario: sa
-- Contraseña: admin
```

3. Actualiza la configuración en `src/main/resources/application.properties` si es necesario:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/llantasdb
spring.datasource.username=sa
spring.datasource.password=admin
```

4. Compila y construye el proyecto:
```bash
mvn clean install
```

## Ejecución

### Modo desarrollo
```bash
mvn spring-boot:run
```

### Empaquetado
```bash
mvn clean package
java -jar target/LlantasApi-0.0.1-SNAPSHOT.jar
```

## Endpoints

La API está disponible en `http://localhost:8080/api/categorias`

### 1. Crear una categoría
- **Método**: POST
- **URL**: `/api/categorias`
- **Body**:
```json
{
  "nombreCategoria": "Neumáticos para SUV",
  "modelo": "Todoterreno",
  "medida": "265/70R17",
  "especificaciones": "Resistencia alta, agarre en barro",
  "tipoRefaccion": "Neumático",
  "compatibilidad": "Vehículos SUV"
}
```
- **Respuesta exitosa**: `201 Created`
- **Errores posibles**: `400 Bad Request`, `409 Conflict` (si ya existe una categoría con el mismo nombre)

### 2. Obtener una categoría por ID
- **Método**: GET
- **URL**: `/api/categorias/{id}`
- **Parámetros**: ID de la categoría
- **Respuesta exitosa**: `200 OK`
```json
{
  "categoriaId": 1,
  "nombreCategoria": "Neumáticos para SUV",
  "modelo": "Todoterreno",
  "medida": "265/70R17",
  "especificaciones": "Resistencia alta, agarre en barro",
  "tipoRefaccion": "Neumático",
  "compatibilidad": "Vehículos SUV",
  "verificarDisponibilidad": null
}
```
- **Errores posibles**: `404 Not Found`

### 3. Listar todas las categorías
- **Método**: GET
- **URL**: `/api/categorias`
- **Respuesta exitosa**: `200 OK`
```json
[
  {
    "categoriaId": 1,
    "nombreCategoria": "Neumáticos para SUV",
    "modelo": "Todoterreno",
    "medida": "265/70R17",
    "especificaciones": "Resistencia alta, agarre en barro",
    "tipoRefaccion": "Neumático",
    "compatibilidad": "Vehículos SUV",
    "verificarDisponibilidad": null
  }
]
```

### 4. Actualizar una categoría
- **Método**: PUT
- **URL**: `/api/categorias/{id}`
- **Parámetros**: ID de la categoría
- **Body**:
```json
{
  "nombreCategoria": "Neumáticos para SUV Actualizado",
  "modelo": "Todoterreno Premium",
  "medida": "265/70R17",
  "especificaciones": "Resistencia alta, agarre en barro y asfalto",
  "tipoRefaccion": "Neumático",
  "compatibilidad": "Vehículos SUV Premium"
}
```
- **Respuesta exitosa**: `200 OK`
- **Errores posibles**: `400 Bad Request`, `404 Not Found`, `409 Conflict`

### 5. Eliminar una categoría
- **Método**: DELETE
- **URL**: `/api/categorias/{id}`
- **Parámetros**: ID de la categoría
- **Respuesta exitosa**: `204 No Content`
- **Errores posibles**: `404 Not Found`

## Documentación interactiva

La API incluye documentación interactiva con Swagger UI disponible en:
- http://localhost:8080/swagger-ui.html
- http://localhost:8080/v3/api-docs

## Pruebas con Postman

Para probar la API con Postman:

1. Importa la colección de Postman (opcional, puedes crearla manualmente)
2. Asegúrate de tener el servidor corriendo en `http://localhost:8080`
3. Configura los headers:
   - `Content-Type: application/json`

### Ejemplos de colección Postman

#### 1. GET - Listar categorías
- URL: `GET http://localhost:8080/api/categorias`
- Headers: `Content-Type: application/json`

#### 2. POST - Crear categoría
- URL: `POST http://localhost:8080/api/categorias`
- Body: Raw JSON
```json
{
  "nombreCategoria": "Neumáticos deportivos",
  "modelo": "Verano",
  "medida": "245/40R18",
  "especificaciones": "Alto rendimiento, asfalto seco",
  "tipoRefaccion": "Neumático",
  "compatibilidad": "Vehículos deportivos"
}
```

#### 3. PUT - Actualizar categoría
- URL: `PUT http://localhost:8080/api/categorias/1`
- Body: Raw JSON
```json
{
  "nombreCategoria": "Neumáticos deportivos actualizados",
  "modelo": "Verano Premium",
  "medida": "245/40R18",
  "especificaciones": "Alto rendimiento, asfalto seco y húmedo",
  "tipoRefaccion": "Neumático",
  "compatibilidad": "Vehículos deportivos"
}
```

#### 4. DELETE - Eliminar categoría
- URL: `DELETE http://localhost:8080/api/categorias/1`

## Base de datos

El proyecto utiliza JPA con Hibernate para mapeo objeto-relacional. La tabla `categoria_productos` se crea automáticamente con el siguiente esquema:

```sql
CREATE TABLE categoria_productos (
    categoria_id BIGSERIAL PRIMARY KEY,
    nombre_categoria VARCHAR(255) NOT NULL,
    modelo VARCHAR(255),
    medida VARCHAR(255),
    especificaciones TEXT,
    tipo_refaccion VARCHAR(255),
    compatibilidad VARCHAR(255),
    verificar_disponibilidad BOOLEAN
);
```

## Manejo de errores

La API devuelve códigos de estado HTTP estándar:

- `200 OK` - Solicitud exitosa
- `201 Created` - Recurso creado exitosamente
- `204 No Content` - Recurso eliminado exitosamente
- `400 Bad Request` - Solicitud inválida
- `404 Not Found` - Recurso no encontrado
- `409 Conflict` - Conflicto (por ejemplo, duplicado)

## Variables de entorno

Puedes sobreescribir las propiedades por defecto usando variables de entorno:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/tu_basedatos
SPRING_DATASOURCE_USERNAME=tu_usuario
SPRING_DATASOURCE_PASSWORD=tu_contraseña
SERVER_PORT=8080
```

## Configuración adicional

En `application.properties` puedes modificar:

- Puerto del servidor (`server.port`)
- Configuración de la base de datos
- Configuración de JPA
- Logging

## Despliegue en producción

Para desplegar en producción:

1. Asegúrate de tener la base de datos configurada
2. Modifica las propiedades según tu entorno de producción
3. Compila el jar con `mvn clean package`
4. Ejecuta con: `java -jar LlantasApi-0.0.1-SNAPSHOT.jar`
5. Considera usar un gestor de procesos como systemd o Docker para mantener la aplicación corriendo

## Seguridad

Esta API está preparada para integrarse con Spring Security. En producción, considera implementar:
- Autenticación JWT
- Autorización RBAC
- CORS configurado apropiadamente
- Filtros de seguridad

## Contribuciones

Las contribuciones son bienvenidas. Por favor, abre un issue o pull request para discutir cambios antes de enviarlos.

## Licencia

Este proyecto está licenciado bajo [la licencia MIT](LICENSE).

## Contacto

Desarrollador: Proyecto Maycollins
Email: [tu_email@example.com]