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
    public void initFirebase() throws IOException {
        try {
            FileInputStream serviceAccount = new FileInputStream(properties.getCredentialsPath());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

            System.out.println("✅ Firebase inicializado correctamente con ruta: " + properties.getCredentialsPath());
        } catch (Exception e) {
            System.err.println("❌ Error inicializando Firebase: " + e.getMessage());
        }
    }
}
