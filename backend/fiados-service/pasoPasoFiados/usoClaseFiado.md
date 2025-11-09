# 🧩 Lógica del dominio — Clase `Fiado`

## 📘 Descripción general

La clase `Fiado` representa el núcleo del **dominio de fiados** dentro del sistema de inventario de la panadería.  
Su propósito es modelar la relación de crédito temporal que se genera cuando un cliente recibe productos sin pagar de inmediato.

Este objeto no solo almacena información, sino que **define las reglas que gobiernan el comportamiento de un fiado**, como:
- Cuándo se puede o no agregar una nueva deuda.
- Cómo se acumulan las observaciones.
- Qué hacer cuando un cliente supera el límite de crédito.
- Cómo se maneja la activación o desactivación de clientes.

---

## 🧱 Atributos y su razón de ser

| Atributo | Tipo | Descripción | Lógica / Justificación |
|-----------|------|-------------|------------------------|
| `id` | `Long` | Identificador único del registro de fiado. | Permite diferenciar los fiados en la base de datos y mantener trazabilidad por cliente. |
| `nombreCliente` | `String` | Nombre del cliente que tiene el fiado. | Se usa como identificador humano y permite búsquedas rápidas si no se trabaja aún con IDs automáticos de usuarios. |
| `numeroCelular` | `String` | Número de teléfono del cliente (opcional). | Facilita contactar al cliente si es necesario, sin ser obligatorio para registrar el fiado. |
| `valorFiado` | `double` | Total acumulado del dinero que el cliente debe. | Representa la deuda actual; se incrementa cada vez que se agrega un nuevo fiado. |
| `limiteFiado` | `double` | Monto máximo permitido para el fiado. | Define el límite de crédito individual; evita que un cliente acumule deuda ilimitada. |
| `aprobadoPorAdmin` | `boolean` | Indica si el administrador autorizó superar el límite. | Implementa control jerárquico; solo el administrador puede aprobar superar el crédito establecido. |
| `observaciones` | `String` | Registro de texto con detalles del fiado. | Se utiliza como bitácora: se concatenan aquí comentarios, pedidos y eventos relevantes (ej: “Cliente desactivado”). |
| `fechaUltimoFiado` | `LocalDateTime` | Fecha y hora del último movimiento. | Permite saber cuándo fue la última actividad en la cuenta del cliente, útil para reportes y auditoría. |
| `activo` | `boolean` | Estado del cliente (activo/inactivo). | Permite desactivar temporalmente clientes sin eliminar su historial, bloqueando nuevos fiados mientras tanto. |

---

## ⚙️ Lógica de negocio aplicada al dominio

### 🧠 1. **Agregar nuevo fiado**
```java
public boolean agregarNuevoFiado(double nuevoValor, String nuevaObservacion)
