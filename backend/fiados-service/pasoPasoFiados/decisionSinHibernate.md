# 🚀 Decisión técnica: No usar Hibernate en fiados-service

## 📘 Contexto

El microservicio **fiados-service** realiza operaciones CRUD simples
y se integrará con **Firebase** como base de datos principal.

## ❌ Razones para no usar Hibernate

1. Hibernate usa un ORM (contexto de persistencia, generación de SQL, lazy loading),
   que implica más consumo de CPU y memoria.
2. Firebase no usa SQL, por lo que Hibernate no aporta valor técnico.
3. El servicio no requiere relaciones complejas ni transacciones ACID.
4. Se busca un microservicio ligero, rápido y de bajo costo.

## ⚙️ Alternativa elegida

Usar el **Firebase Admin SDK** directamente desde la capa `repository`,
manteniendo la estructura por capas y evitando dependencias innecesarias.

## 📈 Beneficios

- Menor consumo de recursos (CPU, RAM y arranque).  
- Despliegue más rápido.  
- Costo cero (Firebase plan gratuito).  
- Fácil migración futura a una base SQL si el proyecto escala.

## 🧩 Conclusión

> Se elimina Hibernate y JPA del proyecto, implementando un repositorio
> basado en el SDK de Firebase.  
> Esta decisión reduce el peso del microservicio y permite un desarrollo
> ágil y funcional desde las primeras fases del MVP.
