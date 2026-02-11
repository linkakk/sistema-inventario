package com.panaderia.fiados.errors;

public class CuentaNoExisteException extends RuntimeException {

    public CuentaNoExisteException(String numeroCelular) {
        super("No existe una cuenta de fiado asociada al número: " + numeroCelular);
    }
}
