# 📘 Guía: Implementación del Repositorio Firestore (FiadoRepository)

## 🎯 Objetivo

Extender la capa `repository` para Firestore y permitir búsquedas por:
- ✅ ID del fiado  
- ✅ clienteId  
- ✅ nombreCliente  

Además, documentar la integración completa con Firebase Admin SDK (.json de credenciales)  
y realizar pruebas CRUD reales desde Postman o `curl`.

---

## 🧭 Paso a paso

### ✅ Paso 0 — Configuración inicial de Firebase

- [x] Crear proyecto en **Firebase Console**
- [x] Habilitar **Firestore (modo production)**
- [x] Ir a **Project Settings → Service accounts → Generate new private key**
- [x] Descargar el archivo `.json`
- [x] Guardarlo localmente y **NO subirlo a GitHub**
- [x] Añadir al `.gitignore` la carpeta `.secrets/`
- [x] Verificar permisos con `chmod 600`
- [x] Confirmar inicialización correcta con mensaje en consola:
  ```
  ✅ Firebase inicializado correctamente.
  ```

---

### [ ] Paso 1 — Crear la estructura base del repositorio

📁 Ruta:
```
src/main/java/com/panaderia/fiados/repository/FiadoRepository.java
```

🧩 Tareas:
- [ ] Crear la interfaz `FiadoRepository`.
- [ ] Inyectar una instancia de `Firestore` usando `FirestoreClient.getFirestore()`.
- [ ] Definir los métodos iniciales:
  - [ ] `save(Fiado fiado)` → crear o actualizar documento.
  - [ ] `findById(String id)` → obtener un documento por ID.
  - [ ] `findAll()` → listar todos los fiados.
  - [ ] `deleteById(String id)` → eliminar un documento.

📘 Notas:
- Firestore es **asíncrono**, usaremos `ApiFuture` o `CompletableFuture` para manejar las operaciones.
- La colección se llamará `"fiados"`.

---

### [ ] Paso 2 — Ajustar el modelo Firestore (`Fiado.java`)

📁 Ruta:
```
src/main/java/com/panaderia/fiados/model/Fiado.java
```

🧩 Campos requeridos:
```java
private String id;
private String clienteId;
private String nombreCliente;
private double valorTotal;
private LocalDate fecha;
private String estado; // pendiente / pagado
private String observaciones; // opcional
```

🧩 Tareas:
- [ ] Confirmar que el modelo tiene **constructor vacío y con parámetros**.
- [ ] Agregar anotaciones de **Lombok** (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`).
- [ ] Asegurar compatibilidad con serialización JSON.

---

### [ ] Paso 3 — Implementar búsquedas personalizadas

📁 Métodos sugeridos en `FiadoRepository`:

```java
List<Fiado> findByClienteId(String clienteId);
List<Fiado> findByNombreCliente(String nombreCliente);
List<Fiado> findByEstado(String estado);
```

🧩 Tareas:
- [ ] Implementar consultas con `whereEqualTo(...)` de Firestore.
- [ ] Probar que devuelven resultados correctos en consola.

---

### [ ] Paso 4 — Crear el servicio (`FiadoService`)

📁 Rutas:
```
src/main/java/com/panaderia/fiados/service/FiadoService.java
src/main/java/com/panaderia/fiados/service/impl/FiadoServiceImpl.java
```

🧩 Tareas:
- [ ] Crear interfaz `FiadoService` con métodos CRUD.
- [ ] Implementar clase `FiadoServiceImpl`.
- [ ] Inyectar `FiadoRepository`.
- [ ] Agregar logs (`log.info(...)`) para depuración.

---

### [ ] Paso 5 — Crear y probar endpoints REST

📁 Controlador:
```
src/main/java/com/panaderia/fiados/api/FiadoController.java
```

🧩 Endpoints CRUD:

| Método | Ruta | Descripción |
|--------|------|--------------|
| `POST` | `/api/fiados` | Crear fiado |
| `GET` | `/api/fiados` | Listar todos |
| `GET` | `/api/fiados/{id}` | Buscar por ID |
| `GET` | `/api/fiados?nombreCliente=...` | Filtrar por nombre |
| `PATCH` | `/api/fiados/{id}/pagar` | Cambiar estado a “pagado” |
| `DELETE` | `/api/fiados/{id}` | Eliminar registro |

📘 Recomendaciones:
- Usar `@Valid` para validar entradas.
- Devolver `ResponseEntity<?>` con mensajes claros y códigos HTTP (`201`, `200`, `404`, etc.).

---

### [ ] Paso 6 — Pruebas con `curl` o Postman

Ejemplos básicos:

```bash
# Crear un fiado
curl -X POST http://localhost:8090/api/fiados -H "Content-Type: application/json" -d '{"nombreCliente":"Don Pedro","valorTotal":45000,"estado":"pendiente","observaciones":"vino el hijo"}'

# Listar fiados
curl http://localhost:8090/api/fiados

# Cambiar estado a pagado
curl -X PATCH http://localhost:8090/api/fiados/{id}/pagar
```

---

### [ ] Paso 7 — Verificación visual en Firestore

1. Abrir la **Firebase Console**.  
2. Ir a **Firestore Database → Colección “fiados”**.  
3. Verificar que:
   - [ ] Los documentos se crean correctamente.  
   - [ ] Los campos coinciden con el modelo.  
   - [ ] Los cambios de estado se reflejan.  
   - [ ] Las eliminaciones se ven en tiempo real.  

---

### [ ] Paso 8 — Documentación y entrega

🧾 Tareas:
- [ ] Crear un archivo `.md` con el resumen del proceso y pruebas realizadas.
- [ ] Adjuntar capturas de pantalla de la consola Firebase y de las pruebas `curl`.
- [ ] Registrar en el `CHANGELOG.md` los nuevos componentes:
  - `FiadoRepository`
  - `FiadoService` / `FiadoServiceImpl`
  - `FiadoController`

---

## ✅ Entregable final del bloque

| Componente | Estado esperado |
|-------------|----------------|
| `FiadoRepository.java` | Implementado y funcional |
| `FiadoServiceImpl.java` | Lógica CRUD estable |
| `FiadoController.java` | Endpoints activos |
| `Firestore` | Colección operativa con datos reales |
| Pruebas `curl` | Completas y verificadas |
| Documentación `.md` | Guardada en `/Documentacion/Firebase/` |

---

## 📘 Próximo bloque

**Integración de DTOs y Validaciones (`@Valid`, `@NotNull`, `@Positive`)**  
para garantizar datos limpios antes de registrar o actualizar fiados.  
Después de esto, comenzaremos con los **tests unitarios** para el servicio.     