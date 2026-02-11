package com.panaderia.fiados.errors;

public class FiadoSuperaLimiteException extends RuntimeException {

    public FiadoSuperaLimiteException(double monto, double limite) {
        super("El fiado supera el límite permitido. Monto actual: " + monto + ", Límite autorizado: " + limite);
    }
}
