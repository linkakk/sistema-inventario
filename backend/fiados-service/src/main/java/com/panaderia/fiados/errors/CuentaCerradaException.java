package com.panaderia.fiados.errors;

public class CuentaCerradaException extends RuntimeException {

    public CuentaCerradaException() {
        super("La cuenta de fiado está cerrada y no permite operaciones.");
    }

    public CuentaCerradaException(String numeroCelular) {
        super("La cuenta de fiado asociada al número " + safe(numeroCelular) + " está cerrada y no permite operaciones.");
    }

    private static String safe(String s) {
        return (s == null || s.isBlank()) ? "N/D" : s;
    }
}
