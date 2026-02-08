package com.panaderia.fiados.model.enums;

/**
 * ============================================================
 * 🔵 Enum: EstadoFiado
 * ------------------------------------------------------------
 * Representa el estado general de la cuenta de fiado.
 * 
 * Cada estado impone restricciones distintas:
 *  - ACTIVO: puede fiar y abonar.
 *  - MOROSO: solo abonos; deuda vencida.
 *  - SUSPENDIDO: suspensión temporal del crédito.
 *  - CASTIGADO: requiere intervención del administrador.
 *
 * Tener esto como enum:
 *  - Asegura consistencia en reglas.
 *  - Evita valores inválidos.
 *  - Permite que Fiado.java aplique lógica basada en estado.
 * ============================================================
 */
public enum EstadoFiado {

    /** Cuenta en buen estado. Puede fiar y abonar. */
    ACTIVO,

    /** Cliente en mora. Solo permite abonos. */
    MOROSO,

    /** Crédito suspendido temporalmente. */
    SUSPENDIDO,

    /** Cliente castigado; requiere aprobación del administrador. */
    CASTIGADO
}
