package com.panaderia.fiados.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

/**
 * Inicializa la conexión con Firebase al arrancar la aplicación.
 * 
 * Se apoya en FirebaseProperties para obtener las rutas configuradas.
 */
@Configuration
public class FirebaseConfig {

    private final FirebaseProperties properties;

    // Inyección por constructor → más limpia y fácil de testear
    public FirebaseConfig(FirebaseProperties properties) {
        this.properties = properties;
    }

  @PostConstruct
public void initFirebase() {
    // Evita re-inicializar si ya existe
    if (!FirebaseApp.getApps().isEmpty()) {
        System.out.println("ℹ️ Firebase ya estaba inicializado.");
        return;
    }

    try {
        // 1) Prioridad: variable de entorno (mejor práctica en prod)
        String ruta = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

        // 2) Fallback: application.properties
        if (ruta == null || ruta.isBlank()) {
            ruta = properties.getCredentialsPath();
        }

        System.out.println("🔍 Usando credenciales Firebase desde: " + ruta);

        try (FileInputStream serviceAccount = new FileInputStream(ruta)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }

        System.out.println("✅ Firebase inicializado correctamente.");
    } catch (Exception e) {
        System.err.println("❌ Error inicializando Firebase: " + e.getMessage());
    }
}

}
