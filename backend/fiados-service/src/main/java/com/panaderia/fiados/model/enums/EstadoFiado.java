package com.panaderia.fiados.model.enums;

/**
 * Estado general de la cuenta de fiado.
 */
public enum EstadoFiado {

    /** Cuenta en buen estado. Puede fiar y abonar. */
    ACTIVO,

    /** Cliente bloqueado: no puede fiar, solo abonar. */
    BLOQUEADO,

    /** Cuenta cerrada definitivamente. */
    CERRADO,

    // (se conservan si ya los venías usando)
    MOROSO,
    SUSPENDIDO,
    CASTIGADO
}
