package com.panaderia.fiados.dto;

import java.time.LocalDate;

/**
 * DTO de salida para devolver información al cliente.
 *
 * ✅ Se usa en las respuestas de la API (GET, POST, PATCH)
 * ✅ Evita enviar objetos internos como Fiado.java
 * ✅ Define el formato final del JSON que recibe el cliente
 *
 * Nota:
 * - Los nombres de los campos deben coincidir con los que se desean exponer.
 */
public record FiadoResponseDTO(

    Long id,                // Identificador único del fiado (autogenerado)
    String nombreCliente,   // Nombre del cliente
    double valorTotal,      // Monto del fiado
    LocalDate fecha,        // Fecha de creación (generada automáticamente)
    String estado,          // Estado actual: "pendiente" o "pagado"
    String observaciones    // Texto libre opcional
) {}
