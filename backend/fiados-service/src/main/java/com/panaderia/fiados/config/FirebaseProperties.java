package com.panaderia.fiados.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Clase que agrupa todas las propiedades relacionadas con Firebase.
 * Permite desacoplar la configuración del código.
 * 
 * Se cargan automáticamente desde application.properties 
 * con el prefijo 'firebase'.
 */
@Component
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {

    /**
     * Ruta local del archivo de credenciales (.json)
     * Ejemplo:
     * firebase.credentials-path=/home/toto/.secrets/firebase/clave.json
     */
    private String credentialsPath;

    // --- GETTERS & SETTERS ---
    public String getCredentialsPath() {
        return credentialsPath;
    }

    public void setCredentialsPath(String credentialsPath) {
        this.credentialsPath = credentialsPath;
    }
}
