package com.panaderia.fiados.errors;

public class CuentaNoExistenteException extends RuntimeException {

    public CuentaNoExistenteException(String referencia) {
        super("No existe cuenta de fiado para: " + referencia);
    }
}

