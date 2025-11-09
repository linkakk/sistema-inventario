# ☁️ Decisión técnica: Uso de Firebase como base de datos del microservicio Fiados

## 📘 Contexto

El microservicio **fiados-service** forma parte del ecosistema del sistema de inventario
de la panadería, encargado de registrar, consultar y administrar los créditos o fiados
otorgados a clientes.

Durante la fase inicial del proyecto (MVP – *Minimum Viable Product*),
se tomó la decisión de utilizar **Firebase** como base de datos principal
por motivos técnicos, económicos y de simplicidad de despliegue.

---

## 💡 Motivos principales de la decisión

### 1️⃣ **Costo cero y acceso inmediato**

Firebase ofrece un plan gratuito (*Spark Plan*) que permite:
- Crear una base de datos funcional sin costo.
- Almacenar información en la nube sin límite de tiempo.
- Acceder con solo una cuenta de Google.
- Desplegar proyectos pequeños sin requerir servidores dedicados.

> 💰 Esto evita gastos en hosting, servidores SQL o infraestructura mientras el sistema crece.

---

### 2️⃣ **Escalabilidad y facilidad de implementación**

Firebase está diseñado para crecer de forma automática sin necesidad de configuración manual.
Aunque actualmente la cantidad de clientes no es significativa,
el sistema puede escalar de forma inmediata en caso de necesitarlo,
simplemente migrando a un plan superior sin cambiar código.

Ventajas prácticas:
- No se requiere instalación local de PostgreSQL o MySQL.
- No hay que configurar servidores ni puertos.
- Ideal para proyectos en desarrollo o prototipos.

---

### 3️⃣ **Integración multiplataforma**

Firebase puede ser accedido desde:
- Aplicaciones **móviles** (Android / React Native).
- Aplicaciones **web** (React, Angular, Next.js).
- Aplicaciones **backend** (Spring Boot usando el Firebase Admin SDK).

Esto permite que **el mismo backend Spring Boot** se comunique con **la app móvil MyRecetario** o el **sistema de inventario web**, centralizando la información en una sola nube.

---

### 4️⃣ **Persistencia simple basada en JSON**

Firebase utiliza un modelo de datos documental basado en JSON (en el caso de Firestore)
o árbol JSON (en Realtime Database), lo que facilita guardar y recuperar información
sin necesidad de esquemas complejos.

Ejemplo de estructura esperada:

```json
"fiados": {
  "cliente_juan_perez": {
    "nombreCliente": "Juan Pérez",
    "numeroCelular": "3001234567",
    "valorFiado": 15000,
    "observaciones": "Pidió pan francés",
    "activo": true,
    "fechaUltimoFiado": "2025-10-29T19:30:00"
  }
}
