package com.panaderia.fiados.model.enums;

/**
 * ============================================================
 * 🔵 Enum: TipoMovimiento
 * ------------------------------------------------------------
 * Representa los tipos de movimientos que pueden ocurrir en la
 * cuenta de fiado de un cliente.
 * 
 * Usar un enum garantiza:
 *  - Opciones limitadas y controladas (no strings libres).
 *  - Seguridad en la lógica del negocio.
 *  - Facilidad para filtrar movimientos por tipo.
 *  - Evitar errores ortográficos o valores inválidos.
 * ============================================================
 */
public enum TipoMovimiento {

    /** Movimiento normal de fiado (el cliente saca productos). */
    FIADO,

    /** Corrección de un movimiento previo (rectificación). */
    RECTIFICACION,

    /** Anulación de un movimiento previo (error o fraude). */
    ANULACION,

    /** Ajuste manual administrativo (casos especiales). */
    AJUSTE
}
