# 🔧 Refactor: Migración de `@Value` a `@ConfigurationProperties` para Firebase

## 🎯 Objetivo

Mejorar la mantenibilidad, escalabilidad y claridad de la configuración de Firebase en el microservicio `fiados-service`, reemplazando el uso de `@Value` por la estrategia profesional recomendada: **`@ConfigurationProperties`**.

Esta mejora permite:
- Agrupar configuraciones relacionadas.
- Evitar propiedades dispersas.
- Facilitar pruebas unitarias.
- Escalar la configuración sin afectar el código fuente.

---

# 📂 Estructura final del módulo

```
backend/fiados-service/
├── src/main/java/com/panaderia/fiados/
│   ├── config/
│   │   ├── firebaseConfig.java          ← inicializa Firebase
│   │   └── firebaseProperties.java      ← carga propiedades firebase.*
│   ├── api/
│   ├── service/
│   ├── repository/
│   └── model/
└── src/main/resources/
    └── application.properties
```

---

# ✅ Paso 1 — Crear `firebaseProperties.java`

**Ruta:** `src/main/java/com/panaderia/fiados/config/firebaseProperties.java`

```java
package com.panaderia.fiados.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Clase que agrupa todas las propiedades relacionadas con Firebase.
 * 
 * Cargadas desde application.properties usando el prefijo 'firebase'.
 */
@Component
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {

    /**
     * Ruta al archivo .json con credenciales de Firebase.
     */
    private String credentialsPath;

    public String getCredentialsPath() { return credentialsPath; }
    public void setCredentialsPath(String credentialsPath) { this.credentialsPath = credentialsPath; }
}
```

---

# ✅ Paso 2 — Modificar `firebaseConfig.java`

**Ruta:** `src/main/java/com/panaderia/fiados/config/firebaseConfig.java`

```java
package com.panaderia.fiados.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Configuración de inicialización de Firebase.
 * 
 * Toma sus parámetros desde FirebaseProperties.
 */
@Configuration
public class FirebaseConfig {

    private final FirebaseProperties properties;

    // Inyección por constructor → recomendada
    public FirebaseConfig(FirebaseProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void initFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream(properties.getCredentialsPath());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

            System.out.println("✅ Firebase inicializado con ruta: " + properties.getCredentialsPath());

        } catch (Exception e) {
            System.err.println("❌ Error inicializando Firebase: " + e.getMessage());
        }
    }
}
```

---

# ✅ Paso 3 — Ajustar `application.properties`

**Ruta:** `src/main/resources/application.properties`

```properties
# Puerto del servicio
server.port=8090

# Configuración Firebase
firebase.credentials-path=/home/toto/.secrets/firebase/claveMicroServicioFiados.json
```

---

# 🔍 Comprobación con logs

Ejecuta:

```bash
./mvnw spring-boot:run
```

Deberías ver:

```
✅ Firebase inicializado con ruta: /home/toto/.secrets/firebase/claveMicroServicioFiados.json
```

---

# 🧠 Beneficios obtenidos

| Mejora | Descripción |
|--------|-------------|
| Configuración agrupada | Todas las propiedades bajo `firebase.*` |
| Tipado fuerte | Spring convierte valores a tipos Java automáticamente |
| Fácil testeo | Es sencillo mockear `FirebaseProperties` en pruebas |
| Escalable | Puedes añadir más campos sin modificar la clase de inicialización |
| Orden y limpieza | Configuración separada de la lógica de inicialización |

---

# 🚀 Próximos pasos sugeridos

- Añadir validación con `@Validated` en `FirebaseProperties`.
- Crear tests unitarios que mockeen `FirebaseProperties`.
- Preparar configuración por perfiles (`application-dev.properties`, `application-prod.properties`).

---

# 📄 Fin del documento

Este archivo explica por qué migramos desde `@Value` hacia `@ConfigurationProperties`,  
cómo se implementó el refactor, y cuál es la estructura final recomendada.
