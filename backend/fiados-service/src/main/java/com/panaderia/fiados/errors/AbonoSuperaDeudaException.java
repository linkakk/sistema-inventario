package com.panaderia.fiados.errors;

public class AbonoSuperaDeudaException extends RuntimeException {

    public AbonoSuperaDeudaException(double montoAbono, double deudaActual) {
        super("El abono (" + montoAbono + ") supera la deuda actual (" + deudaActual + ").");
    }
}

