# 🧠 Impacto y Optimización del IoC (Inversión de Control) en Spring Boot

## 📘 Contexto general

El contenedor **IoC de Spring** administra los objetos llamados *beans*, los cuales son detectados mediante anotaciones como:
- `@Component`
- `@Service`
- `@Repository`
- `@Configuration`

Spring Boot crea, inyecta y destruye estos beans automáticamente según el ciclo de vida de la aplicación.

---

## ⚙️ ¿Consume más recursos que crear los objetos manualmente?

**Sí, ligeramente al inicio.**  
El IoC de Spring escanea los paquetes, detecta anotaciones y registra beans en su contexto.

| Tipo de recurso | Con IoC / Anotaciones | Sin IoC / Manual |
|------------------|------------------------|------------------|
| **Memoria** | Carga todos los beans al inicio. +5–10 MB aprox. | Solo creas lo que usas. |
| **CPU (inicio)** | Escaneo + reflexión. +30–80 ms aprox. | Menor tiempo de arranque. |
| **CPU (ejecución)** | Igual: los beans ya están listos. | Igual. |
| **Mantenibilidad** | Alta (inyección, seguridad, transacciones). | Baja. Riesgo de dependencias mal gestionadas. |

💡 **Conclusión:** el costo inicial es mínimo frente a los beneficios en orden, desacoplamiento y escalabilidad.

---

## 🧩 Ejemplo real: `FirebaseConfig`

### ✅ Con `@Component` (IoC automático)
```java
@Component
public class FirebaseConfig {
    @PostConstruct
    public void initFirebase() {
        System.out.println("✅ Firebase inicializado automáticamente.");
    }
}
➡️ Spring detecta la clase, crea el bean y lo inicializa automáticamente.

❌ Sin @Component (manual)
public class FirebaseConfig {
    public void initFirebase() {
        System.out.println("⚠️ Firebase inicializado manualmente.");
    }
}


Y en el main:

@SpringBootApplication
public class FiadosServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FiadosServiceApplication.class, args);
        FirebaseConfig config = new FirebaseConfig();
        config.initFirebase(); // inicialización manual
    }
}


➡️ Pierdes inyección automática, control de ciclo de vida y compatibilidad con otras configuraciones Spring.

⚙️ Estrategias para optimizar el IoC
1️⃣ Limitar el escaneo de componentes
@SpringBootApplication(scanBasePackages = {"com.panaderia.fiados.service", "com.panaderia.fiados.api"})


Solo se escanean los paquetes necesarios.

2️⃣ Activar inicialización perezosa
spring.main.lazy-initialization=true


Crea los beans solo cuando se usan por primera vez.
Ahorra memoria y acelera el arranque