# ⚙️ Diferencias entre Spring Data JPA y Spring Data JDBC

## 📘 Contexto

Spring Data no es una única herramienta, sino un conjunto de submódulos
que permiten acceder a diferentes tipos de bases de datos bajo una misma filosofía:
usar **repositorios con operaciones CRUD automáticas**.

---

## 🧩 Submódulos relevantes

| Submódulo | Descripción |
|------------|--------------|
| **Spring Data JPA** | Usa Hibernate como ORM para trabajar con bases de datos relacionales. |
| **Spring Data JDBC** | Usa JDBC directo sin ORM, ofreciendo mayor control y simplicidad. |

---

## 🔍 Diferencias clave

| Característica | Spring Data JPA | Spring Data JDBC |
|----------------|-----------------|------------------|
| Motor interno | Hibernate (ORM completo) | JDBC nativo |
| Anotaciones | `@Entity`, `@Id`, `@OneToMany`, etc. | `@Table`, `@Id`, `@Column` |
| Generación de SQL | Automática por Hibernate | Directa por Spring |
| Relaciones | Soportadas automáticamente | Debes manejarlas manualmente |
| Contexto de persistencia | Sí (manejo de entidades en memoria) | No (sin caché ni sincronización) |
| Rendimiento | Menor en microservicios pequeños | Mayor en apps ligeras |
| Complejidad | Alta | Baja |
| Control del SQL | Menor | Total |

---

## 🧱 Cuándo usar cada uno

| Escenario | Recomendación |
|------------|----------------|
| Aplicaciones con muchas relaciones y entidades complejas | ✅ Spring Data JPA |
| Microservicios pequeños con operaciones CRUD simples | ✅ Spring Data JDBC |
| Requiere transacciones avanzadas | ✅ JPA |
| Deseas control manual del SQL | ✅ JDBC |

---

## 🧩 Filosofía

- **JPA (Hibernate):** Piensa en objetos → el framework genera el SQL.  
- **JDBC:** Piensa en tablas → tú defines la estructura y el flujo.

---

## 📘 Conclusión

Ambos son parte del ecosistema **Spring Data**,  
pero están orientados a **necesidades distintas**:

- **Spring Data JPA:** para proyectos grandes, estructurados y con relaciones complejas.  
- **Spring Data JDBC:** para microservicios rápidos y simples donde se busca control directo.

> En términos simples:  
> **JPA abstrae el SQL**,  
> **JDBC lo deja visible.**
