# 📂 Repositorio del módulo Fiados

## 🧭 Descripción general

El paquete `repository` forma parte de la **capa de persistencia** del microservicio.
Su objetivo es proveer una interfaz entre el código Java y la base de datos
sin escribir sentencias SQL manuales.

En este módulo se encuentra el repositorio principal:

### 🧱 `FiadoRepository.java`

**Ubicación:**  
`src/main/java/com/panaderia/fiados/repository/FiadoRepository.java`

**Extiende:**  
`JpaRepository<Fiado, Long>`

---

## ⚙️ Funciones principales

| Método | Descripción |
|--------|--------------|
| `save(Fiado fiado)` | Inserta o actualiza un registro. |
| `findAll()` | Devuelve todos los fiados registrados. |
| `findById(Long id)` | Busca un registro específico por su ID. |
| `deleteById(Long id)` | Elimina un registro. |
| `findByNombreCliente(String nombreCliente)` | Busca un cliente por nombre (método personalizado). |

---

## 🔍 Ejemplo de uso

```java
@Autowired
private FiadoRepository repository;

public void ejemploUso() {
    // Buscar cliente por nombre
    Optional<Fiado> fiado = repository.findByNombreCliente("Juan Pérez");

    // Guardar nuevo fiado
    Fiado nuevo = new Fiado();
    nuevo.setNombreCliente("María López");
    repository.save(nuevo);
}
