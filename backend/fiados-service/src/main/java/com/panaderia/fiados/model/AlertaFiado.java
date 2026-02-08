package com.panaderia.fiados.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

/**
 * ============================================================
 * 📘 AlertaFiado
 * ------------------------------------------------------------
 * Representa una alerta generada internamente sobre la cuenta
 * de fiado de un cliente.
 *
 * Ejemplos:
 *  - Superó el 80% del límite.
 *  - Más de X días sin abonar.
 *  - Deuda muy alta comparada con su historial.
 *
 * Estas alertas pueden mostrarse en el dashboard del sistema.
 * ============================================================
 */
@Data
public class AlertaFiado {

    private UUID id = UUID.randomUUID();

    /** Fecha de generación de la alerta */
    private LocalDateTime fecha = LocalDateTime.now();

    /** Tipo de alerta (ej: "LIMITE_80", "EN_MORA") */
    private String tipo;

    /** Mensaje descriptivo */
    private String mensaje;

    /** Si la alerta ya fue gestionada o marcada como “vista” */
    private boolean atendida = false;


}
