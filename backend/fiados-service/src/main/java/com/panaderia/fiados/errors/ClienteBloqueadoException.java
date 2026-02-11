package com.panaderia.fiados.errors;

public class ClienteBloqueadoException extends RuntimeException {


    public ClienteBloqueadoException() {
        super("La cuenta de fiado está bloqueada y no permite operaciones.");
    }

    public ClienteBloqueadoException(String numeroCelular) {
        super("La cuenta de fiado asociada al número " + safe(numeroCelular) + " está bloqueada y no permite operaciones.");
    }



    private static String safe(String s) {
        return (s == null || s.isBlank()) ? "N/D" : s;
    }
}
