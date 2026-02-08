package com.panaderia.fiados.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.panaderia.fiados.model.enums.TipoMovimiento;
import lombok.Data;

/*
=================================================================================
MovimientoFiado
----------------------------------------------------------------------------------

Representa un movimiento individual dentro del historial
de la cuenta fiado de un cliente.

Cada movimiento puede ser
- FIADO
- RECTIFICACION
- ANULACION
- AJUSTE


Es clave para
- Auditoria
- Trazabilidad
- Reconstruccion del saldo historico
- Consultas detalladas
 */
@Data
public class MovimientoFiado {
    //Identificacion unica de movimiento
    //Esta se debe inicializar para que no sea nula

    private UUID id = UUID.randomUUID();

    
    //Datos principales de movimiento
    private TipoMovimiento tipo;
    private double monto;
    private String descripcion;

    //Metadatos del registro

    //Esta se debe inicializar para que al crear el registro no quede vacia.
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    private String registradoPor;
    private String rolUsuario;
    private String dispositivo;

    //Estado de movimiento (activo/inactivo)
    //Este se debe inicializar para que al crear la transaccion el registro no sea falso y por no se permita su creacion
    private boolean activo = true;


    //Datos de anulacion/rectificacion(solo para historico)

    private String motivoAnulacion;
    private LocalDateTime fechaAnulacion;
    private String anuladoPor;

    
}