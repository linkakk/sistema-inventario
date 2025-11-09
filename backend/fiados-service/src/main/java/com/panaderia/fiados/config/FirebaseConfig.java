package com.panaderia.fiados.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

/**
 * Configuración de Firebase para el microservicio de Fiados.
 *
 * Esta clase permite inicializar la conexión con Firestore
 * usando credenciales seguras. Puede usar la variable de entorno
 * GOOGLE_APPLICATION_CREDENTIALS o, si no está presente, una
 * ruta definida en application.properties.
 */
@Component
public class FirebaseConfig {

        @Value("${firebase.credentials.path}")
        private String firebaseCredentialsPath;

    @PostConstruct
    public void initFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {

            // Intentamos usar variable de entorno primero
            String ruta = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

            if (ruta == null || ruta.isEmpty()) {
                ruta = firebaseCredentialsPath; // fallback
            }

            System.out.println("🔍 Usando credenciales Firebase desde: " + ruta);

            FileInputStream serviceAccount = new FileInputStream(ruta);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase inicializado correctamente.");

        } else {
            System.out.println("ℹ️ Firebase ya estaba inicializado.");
        }
    }
}
