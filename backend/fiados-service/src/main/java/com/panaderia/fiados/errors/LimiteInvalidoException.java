package com.panaderia.fiados.errors;

public class LimiteInvalidoException extends RuntimeException {

    public LimiteInvalidoException(double limite) {
        super("El límite de fiado debe ser mayor a 0. Valor recibido: " + limite);
    }
}

