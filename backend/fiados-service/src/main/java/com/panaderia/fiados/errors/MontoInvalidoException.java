package com.panaderia.fiados.errors;
import com.panaderia.fiados.errors.MontoInvalidoException;



public class MontoInvalidoException extends RuntimeException {

    public MontoInvalidoException(double monto) {
        super("El monto del fiado debe ser mayor a 0. Valor recibido: " + monto);
    }
}
