package com.panaderia.fiados.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.panaderia.fiados.errors.CuentaNoExistenteException;
import com.panaderia.fiados.model.Abono;
import com.panaderia.fiados.model.Fiado;
import com.panaderia.fiados.repository.FiadoRepository;
import com.panaderia.fiados.service.FiadoService;

@Service
public class FiadoServiceImpl implements FiadoService {

    private final FiadoRepository repository;

    public FiadoServiceImpl(FiadoRepository repository) {
        this.repository = repository;
    }

    // ============================================================
    // 🔵 CREAR CUENTA
    // ============================================================
    @Override
    public Fiado crearCuenta(
            String nombreCliente,
            String numeroCelular,
            double limiteFiadoInicial,
            String observacionesIniciales) {

        // Crear cuenta usando el modelo rico
        Fiado fiado = Fiado.crearNuevaCuenta(
                nombreCliente,
                numeroCelular,
                limiteFiadoInicial);

        // Agregar observaciones iniciales si vienen
        if (observacionesIniciales != null && !observacionesIniciales.isBlank()) {
            fiado.setObservaciones(
                fiado.getObservaciones() + " | " + observacionesIniciales
            );
        }

        // Guardar en Firestore
        return repository.save(fiado);
    }

    // ============================================================
    // 🔵 REGISTRAR FIADO
    // ============================================================
@Override
public Fiado registrarFiado(String numeroCelular, double monto, String descripcionMovimiento) {

    Fiado fiado = repository.findByNumeroCelular(numeroCelular)
            .orElseThrow(() -> new CuentaNoExistenteException(numeroCelular));

    fiado.registrarMovimientoFiado(
            monto,
            descripcionMovimiento,
            "sistema",
            "SISTEMA",
            "BACKEND"
    );

    return repository.save(fiado);
}


    // ============================================================
    // 🔵 REGISTRAR ABONO
    // ============================================================
    @Override
    public Fiado registrarAbono(String numeroCelular, Abono abono) {

        Fiado fiado = repository.findByNumeroCelular(numeroCelular)
                .orElseThrow(() -> new IllegalArgumentException("No existe cuenta para ese número"));

        fiado.registrarAbono(abono);

        return repository.save(fiado);
    }

    // ============================================================
    // 🔵 CONSULTAS
    // ============================================================
    @Override
    public Optional<Fiado> buscarPorCelular(String numeroCelular) {
        return repository.findByNumeroCelular(numeroCelular);
    }

    @Override
    public List<Fiado> buscarPorNombre(String nombreCliente) {
        return repository.findByNombreCliente(nombreCliente);
    }

    @Override
    public List<Fiado> listarTodos() {
        return repository.findAll();
    }

    // ============================================================
    // 🔵 ADMINISTRACIÓN DEL CLIENTE
    // ============================================================
    @Override
    public Fiado aprobarLimite(String numeroCelular) {

        Fiado fiado = repository.findByNumeroCelular(numeroCelular)
                .orElseThrow(() -> new IllegalArgumentException("No existe cuenta para ese número"));

        fiado.aprobarPorAdministrador();

        return repository.save(fiado);
    }

    @Override
    public Fiado bloquearCliente(String numeroCelular, String motivo) {

        Fiado fiado = repository.findByNumeroCelular(numeroCelular)
                .orElseThrow(() -> new IllegalArgumentException("No existe cuenta para ese número"));

        fiado.desactivarCliente(motivo);

        return repository.save(fiado);
    }

    @Override
    public Fiado desbloquearCliente(String numeroCelular, String motivo) {

        Fiado fiado = repository.findByNumeroCelular(numeroCelular)
                .orElseThrow(() -> new IllegalArgumentException("No existe cuenta para ese número"));

        fiado.activarCliente(motivo);

        return repository.save(fiado);
    }

    // ============================================================
    // 🔵 ELIMINACIÓN
    // ============================================================
    @Override
    public void eliminarCuentaPorCelular(String numeroCelular) {

        if (numeroCelular == null || numeroCelular.isBlank()) {
            return; // Idempotente
        }

        repository.deleteByNumeroCelular(numeroCelular);
    }
}
