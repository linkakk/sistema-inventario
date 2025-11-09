# Proyecto: Servicio de Fiados (MVP con Observaciones)

## 🎯 Objetivo General
Desarrollar el **MVP del servicio Fiados**, permitiendo registrar, consultar, actualizar y eliminar fiados en memoria.

## 🧭 Estructura base
```
backend/fiados-service/
├── src/main/java/com/panaderia/fiados/
│   ├── api/
│   ├── DTO/    
         ├── FiadoRequestDTO.java
         └── FiadoResponseDTO.jav
│   ├── model/
│   ├── repository/
│   ├── service/
│   └── config/
└── src/main/resources/application.properties
```

Puerto: **8090**  
Arquitectura: **controller → service → repository → model**

---

## 🧩 Paso a paso

### Paso 0 — Crear módulo y puerto
1. Crear carpetas:
   ```bash
   mkdir -p backend/fiados-service/src/main/java/com/panaderia/fiados/{api,service,repository,model,dto,config}
   mkdir -p backend/fiados-service/src/main/resources
   ```
2. Archivo `application.properties`:
   ```properties
   server.port=8090
   spring.application.name=fiados-service
   ```

**Prueba rápida:**  
Ejecutar `./mvnw spring-boot:run` y verificar que escuche en `8090`.

---

### Paso 1 — Modelo y DTOs
Crear `Fiado.java`, `FiadoRequest.java`, `FiadoResponse.java` con validaciones básicas.

**Validaciones:**  
- `@NotBlank` para nombre  
- `@Positive` para valorTotal  
- Fecha y estado autogenerados

---

### Paso 2 — Repositorio en memoria
Crear `FiadoRepository` con `Map<Long, Fiado>` y `AtomicLong` para secuencia.

**Métodos:**  
- save  
- findById  
- findAll  
- delete

---

### Paso 3 — Servicio de dominio
Crear `FiadoService` con reglas de negocio:
- Estado por defecto: `pendiente`
- Filtro por estado (GET ?estado=)
- Cambio a `pagado` (PATCH /pagar)
- Eliminación segura

---

### Paso 4 — Configuración de Beans
Crear `FiadosConfig.java` para exponer beans:
```java
@Configuration
public class FiadosConfig {
  @Bean public FiadoRepository fiadoRepository(){ return new FiadoRepository(); }
  @Bean public FiadoService fiadoService(FiadoRepository repo){ return new FiadoService(repo); }
}
```

---

### Paso 5 — Controlador REST
Endpoints:
- `POST /api/fiados` → crear
- `GET /api/fiados` y `GET /api/fiados?estado=` → listar
- `PATCH /api/fiados/{id}/pagar` → cambiar estado
- `DELETE /api/fiados/{id}` → eliminar

---

### Paso 6 — Test unitario (Service)
Crear `FiadoServiceTest` para probar creación, listado y pago.

**Comando:**  
```bash
./mvnw -q test
```

---

### Paso 7 — Manejo de errores REST
Crear `GlobalExceptionHandler` para 400 y 404:
- `NoSuchElementException` → 404
- `MethodArgumentNotValidException` → 400

---

### Paso 8 — CORS (opcional dev)
Crear `WebConfig` para permitir orígenes en desarrollo.

---

### Paso 9 — Pruebas con curl/httpie
**Crear:**
```bash
http POST :8090/api/fiados nombreCliente="Doña Marta" valorTotal:=20500 observaciones="vino la sobrina"
```
**Listar:**
```bash
http :8090/api/fiados
http :8090/api/fiados estado==pendiente
```
**Pagar:**
```bash
http PATCH :8090/api/fiados/1/pagar
```
**Eliminar:**
```bash
http DELETE :8090/api/fiados/1
```

---

### Paso 10 — Git y documentación
**Commits:**
```bash
git checkout -b feat/fiados-mvp
git add .
git commit -m "feat(fiados): MVP in-memory CRUD con validaciones y errores"
git push -u origin feat/fiados-mvp
```

**CHANGELOG.md:**
- feat: fiados-service (MVP en memoria)
  - POST /api/fiados
  - GET /api/fiados[?estado]
  - PATCH /api/fiados/{id}/pagar
  - DELETE /api/fiados/{id}
  - Validación y manejo de errores básicos

---

## ✅ Checklist final
- [ ] Puerto 8090 configurado  
- [ ] Modelo + DTOs  
- [ ] Repository in-memory  
- [ ] Service con reglas  
- [ ] Controller con endpoints  
- [ ] Handler 400/404  
- [ ] Tests básicos  
- [ ] Curl/httpie funcionando  
- [ ] Commit con Conventional Commits
