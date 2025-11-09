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
 * ============================================
 * 🧾 Clase: Fiado
 * ============================================
 * 
 * Representa un registro de fiado dentro del sistema de la panadería.
 * 
 * Un fiado es una deuda temporal generada por un cliente al solicitar productos
 * sin pagarlos inmediatamente. La clase modela tanto los datos del cliente
 * como las reglas del dominio asociadas al manejo de esa deuda.
 * 
 * La entidad contiene lógica para:
 *  - Registrar nuevos fiados y acumular el total.
 *  - Controlar límites máximos de crédito.
 *  - Requerir aprobación del administrador si se supera el límite.
 *  - Activar o desactivar clientes con fiados.
 *  - Registrar observaciones sobre cada acción.
 * 
 * Esta clase forma parte de la capa de dominio (modelo) y su responsabilidad
 * principal es representar y gestionar el estado de un fiado individual.
 * La persistencia y la coordinación con otras capas se realizan a través de
 * FiadoService y FiadoRepository.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fiados")
public class Fiado {

    /**
     * Identificador único del registro de fiado.
     * Se asigna automáticamente por la base de datos o el sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre completo del cliente que tiene el fiado.
     */
    private String nombreCliente;

    /**
     * Número de contacto del cliente (opcional).
     * Permite notificar o contactar al deudor si lo desea proporcionar.
     */
    private String numeroCelular;

    /**
     * Valor total acumulado del fiado.
     * Cada vez que se agrega un nuevo pedido, este valor se incrementa.
     */
    private double valorFiado;

    /**
     * Límite máximo permitido para el fiado.
     * Si se supera este límite y el cliente no tiene aprobación,
     * la operación será rechazada.
     */
    private double limiteFiado;

    /**
     * Indica si el administrador aprobó superar el límite establecido.
     * Este valor debe ser modificado únicamente por personal autorizado.
     */
    private boolean aprobadoPorAdmin;

    /**
     * Campo de texto que almacena observaciones relacionadas con el fiado.
     * Se van concatenando conforme se agregan nuevos pedidos o cambios de estado.
     */
    private String observaciones;

    /**
     * Fecha y hora del último movimiento del fiado.
     * Se actualiza cada vez que se agrega un nuevo registro o se modifica el estado.
     */
    private LocalDateTime fechaUltimoFiado;

    /**
     * Indica si el cliente está activo para realizar fiados.
     * Si es false, no se podrán registrar nuevos pedidos para este cliente.
     */
    private boolean activo = true;

    // ============================================================
    // 🧠 Métodos de dominio (lógica interna del fiado)
    // ============================================================

    /**
     * Agrega un nuevo fiado al cliente, acumulando el valor y la observación.
     * 
     * Reglas:
     *  - No se puede agregar un fiado si el cliente está inactivo.
     *  - No se puede superar el límite de fiado sin aprobación administrativa.
     *  - Cada nueva observación se concatena al historial existente.
     *
     * @param nuevoValor       Monto del nuevo fiado a registrar.
     * @param nuevaObservacion Observación o descripción del pedido.
     * @return true si el fiado fue agregado correctamente,
     *         false si no cumple las condiciones (inactivo o supera el límite).
     */
    public boolean agregarNuevoFiado(double nuevoValor, String nuevaObservacion) {
        // Bloqueo si el cliente está desactivado
        if (!this.activo) {
            return false;
        }

        double nuevoTotal = this.valorFiado + nuevoValor;

        // Verificación de límite de crédito
        if (nuevoTotal > this.limiteFiado && !this.aprobadoPorAdmin) {
            return false;
        }

        // Actualización del valor total
        this.valorFiado = nuevoTotal;

        // Concatenación de observaciones
        if (this.observaciones == null || this.observaciones.isEmpty()) {
            this.observaciones = nuevaObservacion;
        } else {
            this.observaciones += " | " + nuevaObservacion;
        }

        // Registro de fecha del último movimiento
        this.fechaUltimoFiado = LocalDateTime.now();
        return true;
    }

    /**
     * Marca el fiado como aprobado por un administrador.
     * 
     * Este método permite que el cliente pueda superar su límite de fiado.
     * Debe ser ejecutado únicamente tras la revisión y autorización del administrador.
     */
    public void aprobarPorAdministrador() {
        this.aprobadoPorAdmin = true;
    }

    /**
     * Desactiva al cliente para evitar que siga generando nuevos fiados.
     * 
     * @param motivo Texto descriptivo que justifica la desactivación (ej: "no pagó deuda anterior").
     *               Se agregará al historial de observaciones.
     */
    public void desactivarCliente(String motivo) {
        this.activo = false;
        this.observaciones = (this.observaciones == null ? "" : this.observaciones + " | ")
                + "Cliente desactivado: " + motivo;
    }

    /**
     * Reactiva al cliente para permitirle generar nuevos fiados.
     * Se agrega una observación indicando la reactivación.
     */
    public void activarCliente() {
        this.activo = true;
        this.observaciones = (this.observaciones == null ? "" : this.observaciones + " | ")
                + "Cliente reactivado por administrador.";
    }
}
