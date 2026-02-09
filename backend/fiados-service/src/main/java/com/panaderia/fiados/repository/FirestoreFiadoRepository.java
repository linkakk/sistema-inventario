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
 * Repositorio profesional basado en Firestore
 * ------------------------------------------------------------
 * - documentId YA NO depende del numeroCelular
 * - usamos UUID para asegurar unicidad e inmutabilidad
 * - numeroCelular es OPCIONAL y consultable
 * - nombreCliente puede repetirse (casos reales)
 * - compatibilidad futura con SQL (campo Long id)
 * ============================================================
 */
@Repository
public class FirestoreFiadoRepository implements FiadoRepository {

    private static final String COLLECTION = "fiados";

    /**
     * Obtiene la instancia singleton de Firestore.
     */
    private Firestore db() {
        return FirestoreClient.getFirestore();
    }

    // ============================================================
    // 🔵 MÉTODO SAVE (crea o actualiza)
    // ============================================================
    @Override
    public Fiado save(Fiado fiado) {

        // Si NO tiene ID → ES NUEVO → generar UUID como documentId
        if (fiado.getFirestoreId() == null || fiado.getFirestoreId().isBlank()) {
            String newId = UUID.randomUUID().toString();
            fiado.setFirestoreId(newId);
        }

        // Guardamos en Firestore con su ID inmutable
        DocumentReference ref = db()
                .collection(COLLECTION)
                .document(fiado.getFirestoreId());

        ref.set(fiado);

        return fiado;
    }

    // ============================================================
    // 🔵 Buscar por FirestoreId (ID real del documento)
    // ============================================================
    @Override
    public Optional<Fiado> findById(Long id) {
        // ❌ Este método no está soportado todavía
        throw new UnsupportedOperationException(
            "findById(Long id) no está soportado en Firestore. " +
            "Este método existe solo para futura migración a SQL."
        );
    }

    // ============================================================
    // 🔵 Buscar por numeroCelular (campo opcional)
    // ============================================================
    @Override
    public Optional<Fiado> findByNumeroCelular(String numeroCelular) {

        // Validación: número opcional → no se debe romper
        if (numeroCelular == null || numeroCelular.isBlank()) {
            return Optional.empty();
        }

        try {
            // Firestore NO usa numeroCelular como ID → buscamos por query
            var snap = db()
                .collection(COLLECTION)
                .whereEqualTo("numeroCelular", numeroCelular)
                .get()
                .get();

            if (snap.isEmpty()) {
                return Optional.empty();
            }

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
    // 🔵 Buscar por nombreCliente
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
    // 🔵 Eliminar por numeroCelular
    // ============================================================
    @Override
    public void deleteByNumeroCelular(String numeroCelular) {

        // Buscar primero por query
        var optional = findByNumeroCelular(numeroCelular);

        optional.ifPresent(f -> {
            db()
                .collection(COLLECTION)
                .document(f.getFirestoreId())
                .delete();
        });
    }

    // ============================================================
    // 🔵 Eliminar por ID SQL (NO soportado)
    // ============================================================
    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException(
            "deleteById(Long id) no soportado en Firestore. " +
            "Eliminación debe ser por numeroCelular o firestoreId."
        );
    }
}
