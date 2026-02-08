package com.panaderia.fiados.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================
 * 📘 CambioLimite
 * ------------------------------------------------------------
 * Representa un historial de cambios en el límite de fiado de
 * un cliente.
 *
 * Se usa para tener trazabilidad:
 *  - Quién cambió el límite.
 *  - Cuándo se cambió.
 *  - Por qué se cambió.
 *  - Valor anterior vs valor nuevo.
 *
 * Esto es fundamental para auditoría.
 * ============================================================
 */
@Getter
@Setter
public class CambioLimite {

    private UUID id = UUID.randomUUID();

    /** Límite anterior antes del cambio */
    private double limiteAnterior;

    /** Nuevo límite asignado al cliente */
    private double nuevoLimite;

    /** Fecha exacta del cambio */
    private LocalDateTime fechaCambio = LocalDateTime.now();

    /** Motivo del cambio de límite */
    private String motivo;

    /** Usuario que realizó el cambio (admin o empleado) */
    private String cambiadoPor;

}
