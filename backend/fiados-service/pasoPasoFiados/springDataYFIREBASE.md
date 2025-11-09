# 🔥 Spring Data y Firebase: ¿son compatibles?

## 📘 Contexto

**Spring Data** es un módulo de Spring diseñado para simplificar el acceso a bases de datos,
ya sean relacionales (SQL) o NoSQL (MongoDB, Redis, etc.).

Sin embargo, **Firebase no forma parte del ecosistema soportado por Spring Data**,
ya que utiliza un modelo de almacenamiento documental propio
(Firestore o Realtime Database) que no implementa JPA ni SQL.

---

## 🚫 ¿Por qué no se puede usar directamente?

Firebase no maneja:
- Entidades con `@Entity`
- Operaciones CRUD de JPA
- Consultas basadas en nombres (`findBy...`)
- Transacciones JPA ni `EntityManager`

Por eso, `JpaRepository` o `CrudRepository` **no funcionarán** con Firebase.

---

## ⚙️ Alternativa correcta: Firebase Admin SDK

La conexión se realiza mediante el **Firebase Admin SDK**:

```java
FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(new FileInputStream("firebase-key.json")))
        .setDatabaseUrl("https://<proyecto>.firebaseio.com/")
        .build();

FirebaseApp.initializeApp(options);
