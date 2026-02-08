package com.panaderia.fiados.model;

import lombok.Data;

/**
 * ============================================================
 * 📘 IndicadoresFiado
 * ------------------------------------------------------------
 * Guarda estadísticas internas del comportamiento del cliente
 * con respecto a su fiado.
 *
 * Estos indicadores ayudan a medir riesgo y comportamiento:
 *  - Cuántos fiados ha hecho.
 *  - Cuántos abonos.
 *  - Promedio de fiado.
 *  - Promedio de abono.
 *  - Ratio de pago (% de lo que paga vs. lo que fía).
 *
 * Se recalcula automáticamente desde Fiado.java.
 * ============================================================
 */
@Data
public class IndicadoresFiado {

    /** Cantidad total de movimientos FIADOS */
    private int cantidadFiados;

    /** Cantidad total de abonos */
    private int cantidadAbonos;

    /** Promedio del monto de cada fiado */
    private double promedioFiado;

    /** Promedio del monto de cada abono */
    private double promedioAbono;

    /** Ratio de pago: totalAbonos / totalFiados */
    private double ratioPago;

 
}
