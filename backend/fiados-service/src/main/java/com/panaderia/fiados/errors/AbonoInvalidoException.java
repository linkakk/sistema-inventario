package com.panaderia.fiados.errors;

public class AbonoInvalidoException extends RuntimeException {

    public AbonoInvalidoException() {
        super("El abono no puede ser nulo o inválido.");
    }
}
