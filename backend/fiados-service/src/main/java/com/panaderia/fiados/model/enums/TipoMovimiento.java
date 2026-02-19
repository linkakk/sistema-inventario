package com.panaderia.fiados.model.enums;

/**
 * Tipos de movimientos en una cuenta de fiado.
 */
public enum TipoMovimiento {

    /** El cliente saca productos a crédito. */
    FIADO,

    /** El cliente paga una parte o todo (abono). */
    ABONO,

    /** Corrección de un movimiento previo. */
    RECTIFICACION,

    /** Anulación de un movimiento previo. */
    ANULACION,

    /** Ajuste manual administrativo (puede ser + o -). */
    AJUSTE
}
