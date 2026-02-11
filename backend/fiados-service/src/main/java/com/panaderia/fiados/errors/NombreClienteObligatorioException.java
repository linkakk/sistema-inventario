package com.panaderia.fiados.errors;

public class NombreClienteObligatorioException extends RuntimeException {

    public NombreClienteObligatorioException() {
        super("El nombre del cliente es obligatorio para crear una cuenta de fiado.");
    }
}
