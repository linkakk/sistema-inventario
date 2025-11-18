package com.panaderia.fiados.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================
 * 🧾 MODELO DE DOMINIO: Fiado
 * ------------------------------------------------------------
 * Representa la CUENTA DE CRÉDITO de un cliente:
 *
 *  - Deuda actual (valorFiado)
 *  - Límite de fiado (limiteFiado)
 *  - Estado del cliente (activo / inactivo)
 *  - Autorización especial (aprobadoPorAdmin)
 *  - Historial de abonos (lista de Abono)
 *  - Control de fechas de movimientos
 *
 * 🔥 Importante:
 *  - Este modelo está pensado para Firestore (NoSQL) hoy.
 *  - id (Long) se conserva SOLO para futura migración a SQL.
 *  - NO usamos @Entity ni anotaciones JPA en este MVP.
 *
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fiado {

    // ============================================================
    // 🟦 IDENTIFICADORES
    // ============================================================

    /**
     * ID interno reservado para futura migración a SQL.
     * Hoy NO es utilizado por Firestore.
     */
    private Long id;

    /**
     * ID real del documento en Firestore.
     * - Se usa un UUID generado por el repositorio.
     */
    private String firestoreId;

    // ============================================================
    // 🟦 DATOS DEL CLIENTE
    // ============================================================

    /**
     * Nombre del cliente que tiene la cuenta de fiado.
     */
    private String nombreCliente;

    /**
     * Número de celular del cliente.
     * - Se usa como dato de contacto y posible búsqueda.
     */
    private String numeroCelular;

    // ============================================================
    // 🟦 PARÁMETROS DEL CRÉDITO (CUENTA DE FIADO)
    // ============================================================

    /**
     * Valor TOTAL actual del fiado (deuda vigente).
     * - Se incrementa cuando el cliente fía algo.
     * - Se reduce cuando hace un abono.
     */
    private double valorFiado;

    /**
     * Límite de fiado autorizado para este cliente.
     * - Evita que el crédito sea ilimitado.
     */
    private double limiteFiado;

    /**
     * Indica si un administrador ha autorizado
     * superar el límite de fiado.
     */
    private boolean aprobadoPorAdmin;

    /**
     * Observaciones generales sobre el cliente o su comportamiento.
     * - Se van concatenando eventos importantes.
     */
    private String observaciones;

    /**
     * Fecha y hora del ÚLTIMO fiado (última vez que sacó productos a crédito).
     */
    private LocalDateTime fechaUltimoFiado;

    /**
     * Fecha y hora del ÚLTIMO abono realizado por el cliente.
     */
    private LocalDateTime fechaUltimoAbono;

    /**
     * Fecha en la que se creó la cuenta de fiado.
     */
    private LocalDate fechaCreacion;

    /**
     * Indica si el cliente está habilitado para seguir fiando.
     * - false → puede seguir abonando, pero NO puede sacar más fiado.
     */
    private boolean activo = true;

    // ============================================================
    // 🟦 HISTORIAL DE ABONOS
    // ============================================================

    /**
     * Historial de abonos realizados por el cliente.
     * - Cada abono reduce la deuda (valorFiado).
     * - No se eliminan ni modifican.
     */
    private List<Abono> abonos = new ArrayList<>();

    // ============================================================
    // 🟦 MÉTODOS DE DOMINIO (LÓGICA DE NEGOCIO)
    // ============================================================

    /**
     * Inicializa una nueva cuenta de fiado.
     * Recomendada para usar desde el Service como método de fábrica.
     */
    public static Fiado crearNuevaCuenta(
            String nombreCliente,
            String numeroCelular,
            double limiteFiadoInicial) {

        if (nombreCliente == null || nombreCliente.isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }

        if (limiteFiadoInicial <= 0) {
            throw new IllegalArgumentException("El límite de fiado debe ser mayor a 0.");
        }

        Fiado fiado = new Fiado();
        fiado.setNombreCliente(nombreCliente);
        fiado.setNumeroCelular(numeroCelular);
        fiado.setLimiteFiado(limiteFiadoInicial);
        fiado.setValorFiado(0.0);
        fiado.setAprobadoPorAdmin(false);
        fiado.setActivo(true);
        fiado.setFechaCreacion(LocalDate.now());
        fiado.setObservaciones("Cuenta de fiado creada.");

        return fiado;
    }

    /**
     * Registra un nuevo FIADO (el cliente saca productos a crédito).
     *
     * Reglas:
     * - El cliente debe estar activo.
     * - El monto debe ser > 0.
     * - Si el nuevo total supera límite y NO está aprobadoPorAdmin → no deja.
     *
     * @param montoFiado   valor de los productos fiados en este movimiento.
     * @param descripcion  descripción corta del movimiento (ej: "pan + leche").
     * @return true si el fiado fue aceptado; false si fue rechazado por límite.
     */
    public boolean registrarFiado(double montoFiado, String descripcion) {

        if (!this.activo) {
            // Cliente bloqueado: no puede seguir fiando.
            agregarObservacion("Intento de fiar con cuenta inactiva. Monto: " + montoFiado);
            return false;
        }

        if (montoFiado <= 0) {
            throw new IllegalArgumentException("El monto fiado debe ser mayor a 0.");
        }

        double nuevoTotal = this.valorFiado + montoFiado;

        // Supera límite sin aprobación administrativa
        if (nuevoTotal > this.limiteFiado && !this.aprobadoPorAdmin) {
            agregarObservacion("Fiado rechazado por superar límite. Intento: " + montoFiado);
            return false;
        }

        // Actualizar la deuda total
        this.valorFiado = nuevoTotal;

        // Registrar descripción en observaciones
        if (descripcion != null && !descripcion.isBlank()) {
            agregarObservacion("Fiado registrado: " + montoFiado + " - " + descripcion);
        } else {
            agregarObservacion("Fiado registrado: " + montoFiado);
        }

        // Registrar fecha del último fiado
        this.fechaUltimoFiado = LocalDateTime.now();

        return true;
    }

    /**
     * Registra un ABONO (pago parcial) sobre la deuda actual.
     *
     * Reglas:
     * - El abono no puede ser null.
     * - El monto no puede ser mayor que la deuda actual.
     * - El abono se acumula en el historial.
     * - Se reduce valorFiado.
     *
     * @param abono instancia de Abono ya validada (modelo rico).
     */
    public void registrarAbono(Abono abono) {
        if (abono == null) {
            throw new IllegalArgumentException("El abono no puede ser null.");
        }

        double monto = abono.getMonto();

        if (monto > this.valorFiado) {
            throw new IllegalArgumentException(
                "El monto del abono (" + monto + ") excede la deuda actual (" + this.valorFiado + ")."
            );
        }

        // Registrar en historial
        this.abonos.add(abono);

        // Reducir la deuda
        this.valorFiado -= monto;
        if (this.valorFiado < 0) {
            this.valorFiado = 0; // seguridad extra
        }

        // Fecha de último abono
        this.fechaUltimoAbono = LocalDateTime.now();

        // Observación del abono
        String detalle = "Abono de " + monto
                + ", método: " + abono.getMetodoPago()
                + (abono.getRegistradoPor() != null ? ", registrado por: " + abono.getRegistradoPor() : "")
                + ", fecha abono: " + abono.getFecha();
        agregarObservacion(detalle);
    }

    /**
     * Verifica si el cliente ha alcanzado o superado su límite de fiado.
     *
     * @return true si la deuda actual es mayor o igual al límite.
     */
    public boolean haAlcanzadoLimite() {
        return this.valorFiado >= this.limiteFiado;
    }

    /**
     * Indica si el cliente NO tiene deuda pendiente.
     *
     * @return true si la deuda actual es 0.
     */
    public boolean estaAlDia() {
        return this.valorFiado <= 0;
    }

    /**
     * Marca la cuenta como aprobada por un administrador
     * para poder superar el límite de fiado.
     */
    public void aprobarPorAdministrador() {
        this.aprobadoPorAdmin = true;
        agregarObservacion("Cuenta aprobada por administrador para superar límite.");
    }

    /**
     * Desactiva la cuenta de fiado del cliente.
     * - No se podrá registrar más fiado.
     * - Sí se pueden seguir registrando abonos.
     *
     * @param motivo motivo de la desactivación.
     */
    public void desactivarCliente(String motivo) {
        this.activo = false;
        agregarObservacion("Cliente desactivado: " + motivo);
    }

    /**
     * Reactiva la cuenta de fiado del cliente.
     */
    public void activarCliente(String motivo) {
        this.activo = true;
        agregarObservacion("Cliente reactivado: " + motivo);
    }

    /**
     * Método de utilidad para agregar texto a observaciones
     * sin perder el histórico anterior.
     */
    private void agregarObservacion(String texto) {
        if (texto == null || texto.isBlank()) {
            return;
        }
        if (this.observaciones == null || this.observaciones.isBlank()) {
            this.observaciones = texto;
        } else {
            this.observaciones += " | " + texto;
        }
    }
}
