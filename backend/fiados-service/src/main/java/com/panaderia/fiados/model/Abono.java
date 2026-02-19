package com.panaderia.fiados.model;

import java.time.LocalDate;

import com.panaderia.fiados.errors.AbonoInvalidoException;
import com.panaderia.fiados.errors.MontoInvalidoException;

import lombok.Getter;
import lombok.ToString;

/**
 * ============================================================
 * 📘 Clase: Abono
 * ------------------------------------------------------------
 * Representa un pago parcial aplicado a un fiado.
 *
 * 🔐 MODELO RICO (Domain-Driven Design)
 * ------------------------------------------------------------
 * Esta clase encapsula sus propias reglas internas para garantizar
 * que NINGÚN abono inválido pueda ser creado, independientemente
 * de quién llame al constructor.
 *
 *  ✔ monto > 0
 *  ✔ fecha obligatoria
 *  ✔ método de pago válido
 *  ✔ registradoPor opcional
 *
 * Se usa un método fábrica estático para centralizar validación
 * y evitar inconsistencias.
 *
 * 💾 Serialización:
 * Compatible con Firestore (POJO simple con getters).
 *
 * ============================================================
 */
@Getter
@ToString
public class Abono {

    /**
     * 💵 Monto que se abona al fiado.
     * - Debe ser estrictamente mayor a cero.
     */
    private double monto;

    /**
     * 📅 Fecha en que se realizó el abono.
     * - No puede ser null.
     */
    private LocalDate fecha;

    /**
     * 💳 Método de pago utilizado.
     * - Opcional pero validado si viene.
     * - Ejemplos: "efectivo", "transferencia", "nequi"
     */
    private String metodoPago;

    /**
     * 👤 Empleado que registró el abono.
     * - Controlado desde el frontend.
     * - Opcional.
     */
    private String registradoPor;

    // ============================================================
    // 🔒 Constructor PROTEGIDO
    // ------------------------------------------------------------
    // Impide crear abonos sin pasar por la validación estática.
    // ============================================================
    protected Abono(double monto, LocalDate fecha, String metodoPago, String registradoPor) {
        this.monto = monto;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.registradoPor = registradoPor;
    }

    // ============================================================
    // 🏭 FACTORÍA ESTÁTICA (método recomendado)
    // ------------------------------------------------------------
    // Centraliza validaciones y asegura consistencia.
    // ============================================================
    public static Abono of(double monto,
                           LocalDate fecha,
                           String metodoPago,
                           String registradoPor) {

        // -------------------------
        // Validación: monto
        // -------------------------
        if (monto <= 0) {
            throw new MontoInvalidoException(monto);
        }

        // -------------------------
        // Validación: fecha
        // -------------------------
        if (fecha == null) {
            throw new AbonoInvalidoException();
        }

        // -------------------------
        // Validación: método de pago
        // -------------------------
        if (metodoPago != null && metodoPago.isBlank()) {
            throw new AbonoInvalidoException();
        }

        // Opcional: Default
        if (metodoPago == null) {
            metodoPago = "efectivo"; // estándar panadería
        }

        // -------------------------
        // Validación: registradoPor
        // -------------------------
        if (registradoPor != null && registradoPor.isBlank()) {
            throw new AbonoInvalidoException(); 
        }

        // -------------------------
        // Si todo está bien → crear instancia
        // -------------------------
        return new Abono(monto, fecha, metodoPago, registradoPor);
    }
}
