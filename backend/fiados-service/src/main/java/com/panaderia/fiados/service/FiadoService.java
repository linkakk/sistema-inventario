package com.panaderia.fiados.service;

import java.util.List;
import java.util.Optional;

import com.panaderia.fiados.model.Abono;
import com.panaderia.fiados.model.Fiado;

/**
 * ============================================================
 * 🔵 Interfaz: FiadoService
 * ------------------------------------------------------------
 * Capa de negocio del microservicio FIADOS.
 *
 * - Define QUÉ operaciones se pueden hacer con un fiado.
 * - NO define CÓMO se hacen (eso lo hace FiadoServiceImpl).
 * - Aplica reglas del crédito + reglas del negocio.
 * - Mantiene el dominio desacoplado del Controller y Repository.
 * 
 * Esta interfaz será implementada por:
 *   → com.panaderia.fiados.service.impl.FiadoServiceImpl
 * 
 * ============================================================
 */
public interface FiadoService {

    // ============================================================
    // 🔵 CREACIÓN DE CUENTAS
    // ============================================================

    /**
     * Crea una nueva cuenta de fiado para un cliente.
     *
     * @param nombreCliente         Nombre del titular del crédito.
     * @param numeroCelular         Número del cliente (opcional, puede ser null o vacío).
     * @param limiteFiadoInicial    Límite de crédito asignado al cliente.
     * @param observacionesIniciales Cualquier comentario inicial (opcional).
     * @return Fiado recién creado y persistido.
     */
    Fiado crearCuenta(
            String nombreCliente,
            String numeroCelular,
            double limiteFiadoInicial,
            String observacionesIniciales
    );

    // ============================================================
    // 🔵 REGISTRO DE FIADOS (cliente adquiere productos)
    // ============================================================

    /**
     * Registra un nuevo fiado (compra a crédito) para un cliente.
     *
     * Importante:
     * - El cliente debe estar ACTIVO.
     * - El monto no debe superar el límite (a menos que tenga aprobación administrativa).
     *
     * @param numeroCelular         Celular del cliente para identificar la cuenta (si existe).
     * @param monto                 Monto del fiado que se desea registrar.
     * @param descripcionMovimiento Descripción del movimiento (ej: "pan y leche").
     * @return Cuenta FIADO actualizada.
     */
    Fiado registrarFiado(
            String numeroCelular,
            double monto,
            String descripcionMovimiento
    );

    // ============================================================
    // 🔵 REGISTRO DE ABONOS
    // ============================================================

    /**
     * Registra un abono (pago parcial) sobre la cuenta de un cliente.
     *
     * Reglas:
     * - El monto del abono no puede superar la deuda actual.
     * - El abono debe estar validado por el modelo (monto > 0, fecha válida).
     *
     * @param numeroCelular Celular del cliente (opcional si se buscará por nombre en otra implementación).
     * @param abono         Objeto Abono ya validado por el modelo (modelo rico).
     * @return Cuenta FIADO actualizada.
     */
    Fiado registrarAbono(
            String numeroCelular,
            Abono abono
    );

    // ============================================================
    // 🔵 CONSULTAS
    // ============================================================

    /**
     * Busca una cuenta de fiado por número de celular.
     * Si numeroCelular es null o vacío → devuelve Optional.empty().
     *
     * @param numeroCelular Número a buscar.
     * @return Optional con la cuenta de fiado si existe.
     */
    Optional<Fiado> buscarPorCelular(String numeroCelular);

    /**
     * Busca todas las cuentas cuyo nombre coincida.
     *
     * @param nombreCliente Nombre exacto.
     * @return Lista de fiados encontrados.
     */
    List<Fiado> buscarPorNombre(String nombreCliente);

    /**
     * Retorna todas las cuentas de crédito registradas.
     *
     * @return Lista completa de fiados.
     */
    List<Fiado> listarTodos();

    // ============================================================
    // 🔵 ADMINISTRACIÓN DEL CLIENTE
    // ============================================================

    /**
     * Autoriza que la cuenta pueda superar el límite de crédito.
     *
     * @param numeroCelular Cliente a autorizar.
     * @return Cuenta FIADO actualizada.
     */
    Fiado aprobarLimite(String numeroCelular);

    /**
     * Bloquea al cliente, impidiendo nuevos fiados.
     * El cliente aún puede abonar.
     *
     * @param numeroCelular Cliente a bloquear.
     * @param motivo        Motivo administrativo.
     * @return Cuenta FIADO actualizada.
     */
    Fiado bloquearCliente(String numeroCelular, String motivo);

    /**
     * Reactiva a un cliente, permitiéndole fiar nuevamente.
     *
     * @param numeroCelular Cliente a desbloquear.
     * @param motivo        Motivo administrativo.
     * @return Cuenta FIADO actualizada.
     */
    Fiado desbloquearCliente(String numeroCelular, String motivo);

    // ============================================================
    // 🔵 ELIMINACIÓN
    // ============================================================

    /**
     * Elimina una cuenta de fiado por número de celular.
     * Si no existe, no lanza error (idempotente).
     *
     * @param numeroCelular Número del cliente cuyo fiado se eliminará.
     */
    void eliminarCuentaPorCelular(String numeroCelular);
}
