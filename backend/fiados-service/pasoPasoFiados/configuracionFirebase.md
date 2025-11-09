# 🔥 Configuración de Firebase en Fiados Service (Guía Técnica)

## 🧭 Contexto

El microservicio `fiados-service` pertenece al sistema general **Sistema-Inventario**  
y se encarga de gestionar los fiados de los clientes.  
En esta fase configuramos **Firebase Firestore** como base de datos NoSQL  
para registrar, leer y actualizar los datos del microservicio,  
sin depender todavía de una base relacional.

---

## 🎯 Objetivo general

Inicializar de forma segura la conexión entre el microservicio y **Firebase Firestore**,  
permitiendo que la aplicación se conecte usando un archivo de credenciales  
de una cuenta de servicio.

---

## 🧱 Estructura de carpetas involucradas

backend/fiados-service/
├── src/main/java/com/panaderia/fiados/config/
│ └── FirebaseConfig.java
├── src/main/resources/
│ └── application.properties
└── ~/.secrets/firebase/
└── claveMicroServicioFiados.json



---

## ⚙️ Paso a paso de configuración

### 1️⃣ Creación del proyecto Firebase y la base de datos Firestore

1. Entra a [console.firebase.google.com](https://console.firebase.google.com)
2. Crea un nuevo proyecto llamado **service-fiados**.
3. En la sección **Firestore Database** → clic en **Crear base de datos**.
4. Elige:
   - **Modo:** “Producción”
   - **Región:** `nam5 (us-central)` o `southamerica-east1`
   - **Edición:** “Standard” (no Enterprise)
5. Espera que se cree la base de datos y aparezca el panel con “Iniciar colección”.

---

### 2️⃣ Creación de la cuenta de servicio

1. Ve a **Configuración del proyecto → Cuentas de servicio**.
2. Clic en **Generar nueva clave privada**.
3. Se descargará un archivo `.json` con las credenciales.
4. Guarda ese archivo en tu sistema:
5. /home/toto/.secrets/firebase/claveMicroServicioFiados.json




---

### 3️⃣ Permisos y seguridad del archivo

Dale permisos solo a tu usuario:

```bash
chmod 600 /home/toto/.secrets/firebase/claveMicroServicioFiados.json

4️⃣ Variable de entorno (opcional)

Creamos la variable global:

export GOOGLE_APPLICATION_CREDENTIALS="/home/toto/.secrets/firebase/claveMicroServicioFiados.json"

Para hacerla permanente, agregarla al final del archivo ~/.bashrc:

export GOOGLE_APPLICATION_CREDENTIALS="/home/toto/.secrets/firebase/claveMicroServicioFiados.json"


Luego recargar la sesión:

source ~/.bashrc


5️⃣ Configuración del archivo application.properties

Ruta:

src/main/resources/application.properties


Contenido:

firebase.credentials.path=/home/toto/.secrets/firebase/claveMicroServicioFiados.json


6️⃣ Dependencias necesarias en pom.xml

Agregar las siguientes dependencias dentro de <dependencies>:

<!-- Firebase Admin SDK -->
<dependency>
    <groupId>com.google.firebase</groupId>
    <artifactId>firebase-admin</artifactId>
    <version>9.2.0</version>
</dependency>

<!-- Librería de autenticación -->
<dependency>
    <groupId>com.google.auth</groupId>
    <artifactId>google-auth-library-oauth2-http</artifactId>
    <version>1.19.0</version>
</dependency>

<!-- Utilidades de Google (Guava) -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>32.1.2-jre</version>
</dependency>