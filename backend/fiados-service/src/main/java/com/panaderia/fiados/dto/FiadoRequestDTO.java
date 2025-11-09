package com.panaderia.fiados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * DTO de entrada para registrar un nuevo fiado.
 *
 * Se usa en el endpoint POST /api/fiados.
 * Contiene validaciones básicas para asegurar datos correctos
 * antes de llegar a la capa de servicio.
 *
 * Notas:
 * - @NotBlank: no permite cadenas vacías o nulas.
 * - @Positive: el valor total debe ser mayor que 0.
 * - record: estructura inmutable ideal para transporte de datos (Java 17+).
 */
public record FiadoRequestDTO(

    @NotBlank(message = "El nombre del cliente no puede estar vacío")
    String nombreCliente,

    @Positive(message = "El valor total debe ser mayor que cero")
    double valorTotal,

    String observaciones
) {}
