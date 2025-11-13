package com.panaderia.fiados.dto;

import java.time.LocalDateTime;

/**
 * DTO de salida que representa la información expuesta al cliente.
 *
 * Nunca se envía directamente el modelo Fiado, solo este DTO.
 */
public record FiadoResponseDTO(

        Long id,
        String nombreCliente,
        String numeroCelular,
        double valorFiado,
        double limiteFiado,
        boolean aprobadoPorAdmin,
        boolean activo,
        String observaciones,
        LocalDateTime fechaUltimoFiado

) {}
