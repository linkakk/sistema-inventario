package com.panaderia.fiados.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================
 * 🧾 ENTIDAD: Fiado
 * ------------------------------------------------------------
 * Representa un cliente con fiado y su historial.
 *
 * IMPORTANTE:
 * - id (Long) → reservado para futura migración a SQL (no usado hoy)
 * - firestoreId (String) → ID real del documento en Firestore
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fiados")
public class Fiado {

    // ============================================================
    // 🔵 ID para BASE DE DATOS SQL (FUTURO)
    // ============================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ============================================================
    // 🔵 ID REAL DEL DOCUMENTO EN FIRESTORE (UUID)
    // ============================================================
    private String firestoreId;

    // ============================================================
    // 🔵 DATOS DEL CLIENTE
    // ============================================================
    private String nombreCliente;
    private String numeroCelular;

    // ============================================================
    // 🔵 DATOS DEL FIA DO
    // ============================================================
    private double valorFiado;
    private double limiteFiado;
    private boolean aprobadoPorAdmin;

    private String observaciones;
    private LocalDateTime fechaUltimoFiado;

    private boolean activo = true;

    // ============================================================
    // 🔵 MÉTODOS DE DOMINIO
    // ============================================================

    /**
     * Lógica para agregar un nuevo fiado respetando reglas de negocio.
     */
    public boolean agregarNuevoFiado(double nuevoValor, String nuevaObservacion) {

        // Cliente desactivado → no se puede fiar
        if (!this.activo) {
            return false;
        }

        double nuevoTotal = this.valorFiado + nuevoValor;

        // Supera límite sin autorización
        if (nuevoTotal > this.limiteFiado && !this.aprobadoPorAdmin) {
            return false;
        }

        // Actualizar total
        this.valorFiado = nuevoTotal;

        // Agregar observación
        if (this.observaciones == null || this.observaciones.isBlank()) {
            this.observaciones = nuevaObservacion;
        } else {
            this.observaciones += " | " + nuevaObservacion;
        }

        // Registrar fecha de último movimiento
        this.fechaUltimoFiado = LocalDateTime.now();

        return true;
    }

    /**
     * Autoriza superar límite de fiado.
     */
    public void aprobarPorAdministrador() {
        this.aprobadoPorAdmin = true;
    }

    /**
     * Desactiva cliente y documenta motivo.
     */
    public void desactivarCliente(String motivo) {
        this.activo = false;
        this.observaciones =
            (this.observaciones == null ? "" : this.observaciones + " | ")
            + "Cliente desactivado: " + motivo;
    }

    /**
     * Reactiva cliente.
     */
    public void activarCliente() {
        this.activo = true;
        this.observaciones =
            (this.observaciones == null ? "" : this.observaciones + " | ")
            + "Cliente reactivado por administrador.";
    }
}
