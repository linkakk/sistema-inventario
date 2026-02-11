package com.panaderia.fiados.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.panaderia.fiados.errors.ClienteBloqueadoException;
import com.panaderia.fiados.errors.CuentaCerradaException;
import com.panaderia.fiados.errors.FiadoSuperaLimiteException;
import com.panaderia.fiados.errors.LimiteInvalidoException;
import com.panaderia.fiados.errors.NombreClienteObligatorioException;
import com.panaderia.fiados.model.enums.EstadoFiado;
import com.panaderia.fiados.model.enums.TipoMovimiento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================
 * 🧾 MODELO DE DOMINIO: Fiado
 * ------------------------------------------------------------
 * Representa la CUENTA DE CRÉDITO de un cliente.
 *
 * Es el "agregado raíz" del módulo de fiados:
 *  - Controla la deuda actual (valorFiado)
 *  - Lleva límite de crédito (limiteFiado)
 *  - Gestiona estado del cliente (activo / inactivo / cerrado)
 *  - Registra historial de ABONOS y MOVIMIENTOS
 *  - Mantiene indicadores e historial administrativo
 *
 * 🔥 Importante:
 *  - Pensado inicialmente para Firestore (NoSQL).
 *  - El campo id (Long) se conserva para futura migración a SQL.
 *  - NO usamos @Entity ni anotaciones JPA en este MVP.
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
     * - Puede ser null o vacío si el cliente no tiene celular.
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
    // 🟦 HISTORIAL DE ABONOS (modelo previo, se mantiene por compatibilidad)
    // ============================================================

    /**
     * Historial de abonos realizados por el cliente.
     * - Cada abono reduce la deuda (valorFiado).
     * - No se eliminan ni modifican.
     * - Se mantiene por compatibilidad con el diseño anterior.
     */
    private List<Abono> abonos = new ArrayList<>();

    // ============================================================
    // 🟦 NUEVAS ESTRUCTURAS DE TRAZABILIDAD
    // ============================================================

    /**
     * Lista de movimientos asociados a esta cuenta de fiado.
     * - Incluye fiados, abonos, ajustes, etc.
     * - Es la base para auditoría y reconstrucción de saldo.
     */
    private List<MovimientoFiado> movimientos = new ArrayList<>();

    /**
     * Estado de la cuenta de fiado:
     * - ACTIVO: puede seguir fiando.
     * - EN_MORA / BLOQUEADO: no puede fiar, solo abonar.
     * - CERRADO: cuenta cerrada definitivamente.
     */
    private EstadoFiado estadoFiado = EstadoFiado.ACTIVO;

    /**
     * Historial de cambios de límite de crédito.
     * - Útil para auditoría administrativa.
     */
    private List<CambioLimite> cambioLimites = new ArrayList<>();

    /**
     * Alertas generadas por el comportamiento de la cuenta:
     * - acercamiento al límite
     * - demora en pagos
     * - patrones de riesgo, etc.
     */
    private List<AlertaFiado> alertas = new ArrayList<>();

    /**
     * Indicadores agregados de la cuenta:
     * - cantidad de fiados
     * - monto total fiado
     * - monto total abonado
     * - días en mora, etc.
     *
     * El modelo exacto vive en IndicadoresFiado.
     */
    private IndicadoresFiado indicadores = new IndicadoresFiado();

    // ============================================================
    // 🟦 DATOS DE CUENTA CERRADA
    // ============================================================

    /**
     * Indica si la cuenta fue cerrada definitivamente.
     */
    private boolean cuentaCerrada = false;

    /**
     * Fecha en que se cerró la cuenta de fiado.
     */
    private LocalDateTime fechaCierre;

    /**
     * Motivo administrativo del cierre de la cuenta.
     */
    private String motivoCierre;

    // ============================================================
    // 🟦 DATOS HISTÓRICOS
    // ============================================================

    /**
     * Mayor deuda que ha tenido esta cuenta en su historia.
     * - Útil para análisis de riesgo.
     */
    private double deudaMaximaHistorica;

    /**
     * Momento en que se alcanzó la deuda máxima histórica.
     */
    private LocalDateTime fechaDeudaMaxima;

    // ============================================================
    // 🟦 NOTAS ADMINISTRATIVAS
    // ============================================================

    /**
     * Notas libres dejadas por administradores o supervisores.
     * - No afectan la lógica, solo son informativas.
     */
    private List<String> notasAdministrativas = new ArrayList<>();

    // ============================================================
    // 🟦 FÁBRICA DE CUENTAS
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
            throw new NombreClienteObligatorioException();
        }

        if (limiteFiadoInicial <= 0) {
            throw new LimiteInvalidoException(limiteFiadoInicial);
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

    // ============================================================
    // 🟦 LEGADO: registrarFiado (VERSIÓN SIMPLE)
    // ============================================================

    /**
     * ⚠ Método legado.
     * Mantiene compatibilidad con código anterior que espera un boolean.
     *
     * Internamente delega en registrarMovimientoFiado(...)
     * y devuelve true si el registro fue exitoso.
     */
    @Deprecated
    public boolean registrarFiado(double montoFiado, String descripcion) {
        MovimientoFiado movimiento = registrarMovimientoFiado(
                montoFiado,
                descripcion,
                "sistema",          // usuario genérico si no se envía desde UI
                "SISTEMA",
                "BACKEND"
        );
        // Si no lanzó excepción, se considera exitoso.
        return movimiento != null;
    }

    // ============================================================
    // 🟦 NUEVO: REGISTRO DE MOVIMIENTOS DE FIADO
    // ============================================================

    /**
     * Registra un nuevo movimiento de FIADO (cliente saca productos a crédito).
     *
     * Reglas:
     * - La cuenta no debe estar CERRADA.
     * - La cuenta debe estar ACTIVA.
     * - El monto debe ser > 0.
     * - Se actualiza valorFiado.
     * - Se registra el movimiento en la lista de movimientos.
     * - Se actualizan indicadores y deuda máxima histórica.
     *
     * @param monto         Valor del fiado de este movimiento.
     * @param descripcion   Descripción del movimiento (ej: "pan + leche").
     * @param registradoPor Usuario que registra el movimiento.
     * @param rolUsuario    Rol del usuario ("CAJERO", "ADMIN", etc.).
     * @param dispositivo   Identificador del dispositivo (caja, móvil, etc.).
     * @return MovimientoFiado recién creado y asociado a la cuenta.
     */
    public MovimientoFiado registrarMovimientoFiado(
            double monto,
            String descripcion,
            String registradoPor,
            String rolUsuario,
            String dispositivo) {

        // 1. Validaciones de estado de la cuenta
        if (cuentaCerrada) {
            throw new CuentaCerradaException(this.numeroCelular);}

        if (!activo) {
            throw new ClienteBloqueadoException(this.numeroCelular);}


        // ============================================================
        // 2) VALIDACIONES DE ENTRADA
        // ====================================================
    

        if (monto <= 0) {
            throw new MontoInvalidoException(monto);}



        // ============================================================
        // 3) VALIDAR LÍMITE DE CRÉDITO
        // ============================================================
    

        // Validar límite de crédito (si no hay aprobación admin)
        double nuevoTotal = this.valorFiado + monto;
        
        if (nuevoTotal > this.limiteFiado && !this.aprobadoPorAdmin) {
            throw new FiadoSuperaLimiteException(nuevoTotal,this.limiteFiado);
        }

        // ============================================================
        // 4) CREAR MOVIMIENTO
        // ============================================================
    
        MovimientoFiado movimiento = new MovimientoFiado();
        movimiento.setTipo(TipoMovimiento.FIADO);
        movimiento.setMonto(monto);
        movimiento.setDescripcion(descripcion);
        movimiento.setRegistradoPor(registradoPor);
        movimiento.setRolUsuario(rolUsuario);
        movimiento.setDispositivo(dispositivo);
        movimiento.setActivo(true); // movimiento vigente

        // 4. Agregar a la lista de movimientos
        this.movimientos.add(movimiento);

        // 5. Actualizar deuda actual y metadatos
        this.valorFiado = nuevoTotal;
        this.fechaUltimoFiado = LocalDateTime.now();
        agregarObservacion("Fiado registrado: " + monto +
                (descripcion != null && !descripcion.isBlank() ? " - " + descripcion : "") +
                (registradoPor != null ? " (por: " + registradoPor + ")" : ""));

        // 6. Actualizar deuda máxima histórica
        actualizarDeudaMaxima();

        // 7. Actualizar indicadores básicos (stub para crecer)
        actualizarIndicadoresBasicos(monto);

        // 8. Generar alertas si corresponde (stub)
        evaluarAlertasBasicas();

        return movimiento;
    }

    // ============================================================
    // 🟦 REGISTRO DE ABONOS (integrado con movimientos)
    // ============================================================

    /**
     * Registra un ABONO (pago parcial) sobre la deuda actual.
     *
     * Reglas:
     * - El abono no puede ser null.
     * - El monto no puede ser <= 0.
     * - El monto no puede ser mayor que la deuda actual.
     * - El abono se acumula en el historial legado (abonos).
     * - Se registra también un MovimientoFiado de tipo ABONO.
     * - Se reduce valorFiado.
     *
     * @param abono instancia de Abono ya validada en el nivel de modelo.
     */
    public void registrarAbono(Abono abono) {
        if (abono == null) {
            throw new IllegalArgumentException("El abono no puede ser null.");
        }

        double monto = abono.getMonto();

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del abono debe ser mayor a 0.");
        }

        if (monto > this.valorFiado) {
            throw new IllegalArgumentException(
                    "El monto del abono (" + monto + ") excede la deuda actual (" + this.valorFiado + ").");
        }

        // 1. Registrar en historial legado
        this.abonos.add(abono);

        // 2. Crear movimiento de ABONO
        MovimientoFiado movimiento = new MovimientoFiado();
        movimiento.setTipo(TipoMovimiento.ABONO);
        movimiento.setMonto(monto);
        movimiento.setDescripcion(abono.getDescripcion());
        movimiento.setRegistradoPor(abono.getRegistradoPor());
        movimiento.setRolUsuario("CAJERO"); // o derivado del contexto
        movimiento.setDispositivo("BACKEND");
        movimiento.setActivo(true);

        this.movimientos.add(movimiento);

        // 3. Reducir la deuda
        this.valorFiado -= monto;
        if (this.valorFiado < 0) {
            this.valorFiado = 0; // seguridad extra
        }

        // 4. Fecha de último abono
        this.fechaUltimoAbono = LocalDateTime.now();

        // 5. Observación del abono
        String detalle = "Abono de " + monto
                + (abono.getMetodoPago() != null ? ", método: " + abono.getMetodoPago() : "")
                + (abono.getRegistradoPor() != null ? ", registrado por: " + abono.getRegistradoPor() : "")
                + (abono.getFecha() != null ? ", fecha abono: " + abono.getFecha() : "");
        agregarObservacion(detalle);

        // 6. Actualizar indicadores (stub)
        actualizarIndicadoresBasicos(-monto);

        // 7. Evaluar alertas (por ejemplo, cuenta al día)
        evaluarAlertasBasicas();
    }

    // ============================================================
    // 🟦 CONSULTAS DE ESTADO
    // ============================================================

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
        this.estadoFiado = EstadoFiado.BLOQUEADO;
        agregarObservacion("Cliente desactivado: " + motivo);
    }

    /**
     * Reactiva la cuenta de fiado del cliente.
     */
    public void activarCliente(String motivo) {
        this.activo = true;
        this.estadoFiado = EstadoFiado.ACTIVO;
        agregarObservacion("Cliente reactivado: " + motivo);
    }

    /**
     * Cierra definitivamente la cuenta de fiado.
     * - Se puede usar para clientes que no vuelven a fiar.
     */
    public void cerrarCuenta(String motivo) {
        this.cuentaCerrada = true;
        this.motivoCierre = motivo;
        this.fechaCierre = LocalDateTime.now();
        this.estadoFiado = EstadoFiado.CERRADO;
        agregarObservacion("Cuenta cerrada: " + motivo);
    }

    // ============================================================
    // 🟦 HERRAMIENTAS DE AUDITORÍA / RECONSTRUCCIÓN
    // ============================================================

    /**
     * Recalcula el saldo (valorFiado) a partir de la lista de movimientos.
     * Útil para auditoría o para corregir inconsistencias.
     *
     * Regla:
     * - FIADO y AJUSTE POSITIVO SUMAN.
     * - ABONO y AJUSTE NEGATIVO RESTAN.
     */
    public void recalcularSaldoDesdeMovimientos() {
        double saldo = 0.0;

        for (MovimientoFiado mov : movimientos) {
            if (!mov.isActivo()) {
                continue;
            }

            if (mov.getTipo() == TipoMovimiento.FIADO) {
                saldo += mov.getMonto();
            } else if (mov.getTipo() == TipoMovimiento.ABONO) {
                saldo -= mov.getMonto();
            } else if (mov.getTipo() == TipoMovimiento.AJUSTE) {
                // Los ajustes pueden ser positivos o negativos según el signo del monto
                saldo += mov.getMonto();
            }
        }

        this.valorFiado = Math.max(0.0, saldo);
    }

    // ============================================================
    // 🟦 MÉTODOS PRIVADOS DE SOPORTE
    // ============================================================

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

    /**
     * Actualiza la deuda máxima histórica si el valor actual
     * supera el máximo registrado.
     */
    private void actualizarDeudaMaxima() {
        if (this.valorFiado > this.deudaMaximaHistorica) {
            this.deudaMaximaHistorica = this.valorFiado;
            this.fechaDeudaMaxima = LocalDateTime.now();
        }
    }

    /**
     * Actualiza indicadores básicos de la cuenta.
     * - Por ahora es un stub: aquí se ajustarían conteos y totales.
     *
     * @param deltaMonto Monto del movimiento (positivo para fiado, negativo para abono).
     */
    private void actualizarIndicadoresBasicos(double deltaMonto) {
        // TODO: actualizar campos concretos de IndicadoresFiado
        // Ejemplo: indicadores.incrementarTotalFiado(deltaMonto);
        // Se deja como punto de extensión.
    }

    /**
     * Analiza el estado actual y genera alertas básicas:
     * - acercamiento al límite (>80%)
     * - cuenta en cero después de estar en mora, etc.
     *
     * Por ahora solo es un punto de extensión para futuras reglas.
     */
    private void evaluarAlertasBasicas() {
        // TODO: implementar generación de alertas según reglas de negocio
        // Ejemplo de regla:
        // if (valorFiado >= limiteFiado * 0.8) { crear alerta de "cerca del límite" }
    }
}
