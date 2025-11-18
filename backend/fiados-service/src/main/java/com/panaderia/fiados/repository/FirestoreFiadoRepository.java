package com.panaderia.fiados.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.panaderia.fiados.model.Fiado;

/**
 * ============================================================
 * 🔥 FirestoreFiadoRepository
 * ------------------------------------------------------------
 * Repositorio profesional basado en Firestore.
 * - documentId YA NO depende del numeroCelular.
 * - usamos UUID para asegurar unicidad e inmutabilidad.
 * - numeroCelular es OPCIONAL y consultable.
 * - nombreCliente puede repetirse (casos reales).
 * - compatibilidad futura con SQL (campo Long id).
 * ============================================================
 */
@Repository
@SuppressWarnings("null") // Evita advertencias por valores opcionales en Firestore
public class FirestoreFiadoRepository implements FiadoRepository {

    private static final String COLLECTION = "fiados";

    /**
     * Obtiene la instancia singleton de Firestore.
     */
    private Firestore db() {
        return FirestoreClient.getFirestore();
    }

    // ============================================================
    // 🔵 MÉTODO SAVE — crea o actualiza un documento
    // ============================================================
    @Override
    public Fiado save(Fiado fiado) {

        // Si NO tiene firestoreId → es nuevo → generar UUID como documentId
        if (fiado.getFirestoreId() == null || fiado.getFirestoreId().isBlank()) {
            String newId = UUID.randomUUID().toString();
            fiado.setFirestoreId(newId);
        }

        // Guardamos en Firestore con su ID inmutable
        DocumentReference ref = db()
                .collection(COLLECTION)
                .document(fiado.getFirestoreId());

        ref.set(fiado); // operación asíncrona, no bloqueante

        return fiado;
    }

    // ============================================================
    // 🔵 Buscar por numeroCelular (campo opcional)
    // ============================================================
    @Override
    public Optional<Fiado> findByNumeroCelular(String numeroCelular) {

        // Validación: número opcional → si no hay dato, devolvemos vacío
        if (numeroCelular == null || numeroCelular.isBlank()) {
            return Optional.empty();
        }

        try {
            // Firestore NO usa numeroCelular como ID → se busca con una query
            var snap = db()
                .collection(COLLECTION)
                .whereEqualTo("numeroCelular", numeroCelular)
                .get()
                .get();

            if (snap.isEmpty()) {
                return Optional.empty();
            }

            // Devuelve el primer resultado (en Firestore pueden existir duplicados)
            return Optional.ofNullable(
                snap.getDocuments().get(0).toObject(Fiado.class)
            );

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(
                "Error en Firestore al buscar por numeroCelular", e
            );
        }
    }

    // ============================================================
    // 🔵 Buscar por nombreCliente (puede haber varios)
    // ============================================================
    @Override
    public List<Fiado> findByNombreCliente(String nombreCliente) {
        try {
            var query = db()
                .collection(COLLECTION)
                .whereEqualTo("nombreCliente", nombreCliente)
                .get()
                .get();

            return query.getDocuments()
                    .stream()
                    .map(doc -> doc.toObject(Fiado.class))
                    .collect(Collectors.toList());

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(
                "Error en Firestore al buscar por nombreCliente", e
            );
        }
    }

    // ============================================================
    // 🔵 Obtener todos los fiados
    // ============================================================
    @Override
    public List<Fiado> findAll() {
        try {
            var query = db()
                .collection(COLLECTION)
                .get()
                .get();

            return query.getDocuments()
                    .stream()
                    .map(doc -> doc.toObject(Fiado.class))
                    .collect(Collectors.toList());

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(
                "Error en Firestore al obtener todos los fiados", e
            );
        }
    }

    // ============================================================
    // 🔵 Eliminar por numeroCelular (seguro)
    // ============================================================
    @Override
    public void deleteByNumeroCelular(String numeroCelular) {

        // Buscar primero por query para obtener firestoreId
        var optional = findByNumeroCelular(numeroCelular);

        optional.ifPresent(f -> {
            db()
                .collection(COLLECTION)
                .document(f.getFirestoreId())
                .delete();
        });
    }

    // ============================================================
    // 🔵 Eliminar por ID SQL (NO soportado aún)
    // ============================================================
    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException(
            "deleteById(Long id) no soportado en Firestore. " +
            "Eliminación debe ser por numeroCelular o firestoreId."
        );
    }

    // ============================================================
    // 🚫 findById — No implementado (migración futura a SQL)
    // ============================================================
    
    @Override
    public Optional<Fiado> findById(Long id) {
        // Método reservado para compatibilidad con JPA/SQL
        // Actualmente Firestore no usa este campo.
        throw new UnsupportedOperationException(
            "findById(Long id) no está soportado en Firestore. " +
            "Use findByNumeroCelular() o findByNombreCliente()."
        );
    }

    
    
}
