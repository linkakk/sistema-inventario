package com.panaderia.fiados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * DTO de entrada para crear un fiado o agregar uno nuevo a un cliente existente.
 * 
 * Reglas:
 * - nombreCliente: obligatorio, no vacío
 * - valorFiado: debe ser mayor que 0
 * - numeroCelular: opcional
 * - observaciones: opcional
 */
public record FiadoRequestDTO(

        @NotBlank(message = "El nombre del cliente no puede estar vacío")
        String nombreCliente,

        @Positive(message = "El valor del fiado debe ser mayor que cero")
        double valorFiado,

        String numeroCelular,

        String observaciones

) {}
