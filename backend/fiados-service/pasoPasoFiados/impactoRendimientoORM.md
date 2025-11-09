# ⚙️ Impacto en rendimiento al usar un ORM (Hibernate)

## 📘 Contexto

Hibernate, incluido en Spring Data JPA, es un ORM (Object-Relational Mapper)
que automatiza la conversión entre objetos Java y tablas SQL.

Esa automatización simplifica enormemente el desarrollo,
pero introduce un **consumo adicional de recursos** (CPU, memoria y tiempo)
debido al procesamiento interno que realiza.

---

## 🧩 Procesos internos de Hibernate

| Proceso | Descripción | Impacto |
|----------|--------------|----------|
| Contexto de persistencia | Hibernate mantiene entidades cargadas en memoria. | Mayor uso de memoria |
| Lazy Loading | Carga diferida de relaciones. | Más consultas SQL |
| Generación dinámica de SQL | Interpreta anotaciones y construye SQL al vuelo. | Más CPU |
| Mapeo Objeto-Relacional | Convierte filas ↔ objetos. | CPU y RAM |
| Gestión de transacciones | Controla commits y rollbacks. | Tiempo de procesamiento |

---

## ⚡ Consecuencia

- **Mayor consumo de memoria** cuando se manejan muchas entidades.  
- **Consultas menos eficientes** si se abusa del lazy loading o las relaciones automáticas.  
- **Mayor latencia inicial** por la creación del contexto y las sesiones.

---

## 💡 Ventajas que compensan el costo

- Eliminación de SQL manual.
- Relaciones automáticas (`@OneToMany`, `@ManyToOne`).
- Transacciones y rollback integrados.
- Migración entre bases de datos sin cambiar el código.
- Mantenimiento más fácil a largo plazo.

---

## 🧭 Cuándo evitarlo

| Escenario | Alternativa |
|------------|--------------|
| Microservicios simples | Spring Data JDBC |
| Operaciones masivas de lectura/escritura | SQL manual / MyBatis |
| Aplicaciones móviles / Firebase | NoSQL / SDK nativo |
| Requerimiento de alta eficiencia | JDBC directo |

---

## 🧰 Recomendaciones

- `spring.jpa.open-in-view=false`
- Usar `FetchType.LAZY` para evitar cargas innecesarias.
- Revisar logs de SQL (`spring.jpa.show-sql=true`).
- Agrupar operaciones masivas (`batch_size`).

---

## 📘 Conclusión

Usar un ORM como Hibernate **consume más recursos**,
pero a cambio ofrece **productividad, mantenibilidad y robustez**.

> En proyectos grandes: vale la pena.  
> En microservicios o MVPs pequeños: es mejor algo más liviano como JDBC o Firebase.
