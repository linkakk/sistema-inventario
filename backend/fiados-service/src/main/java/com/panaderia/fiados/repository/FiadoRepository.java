package com.panaderia.fiados.repository;

import java.util.List;
import java.util.Optional;

import com.panaderia.fiados.model.Fiado;

/**
 * ============================================================
 * 📂 Interfaz: FiadoRepository
 * ============================================================
 *
 * Esta interfaz define el contrato que debe cumplir cualquier
 * implementación del repositorio encargado de persistir fiados.
 *
 * 🚫 IMPORTANTE:
 * ------------------------------------------------------------
 *  No extiende de JpaRepository ni usa JPA.
 *  Esto se debe a que el microservicio FIADOS usa Firestore
 *  (NoSQL), por lo que se requiere una implementación manual.
 *
 *  Aun así, mantenemos la interfaz para cumplir principios de:
 *  - arquitectura limpia
 *  - inversión de dependencias (DIP)
 *  - desacoplamiento entre dominio y persistencia
 *  - capacidad de migrar a SQL en el futuro sin romper nada
 * ------------------------------------------------------------
 *
 * 🔐 DUAL-KEY DOMAIN MODEL:
 * ------------------------------------------------------------
 *  - `numeroCelular` es la clave primaria *lógica* en Firestore.
 *  - `id` (Long) se mantiene como clave primaria interna para
 *    una futura migración a SQL sin rehacer los modelos.
 * ------------------------------------------------------------
 *
 * Esta separación evita deuda técnica y permite escalar.
 */
public interface FiadoRepository {

    /**
     * Guarda o actualiza un fiado en el sistema.
     *
     * 📌 Firestore:
     *      - Usa `numeroCelular` como ID del documento.
     * 📌 SQL futuro:
     *      - Usaría `id` como clave primaria (autoincremental).
     *
     * @param fiado objeto de dominio que se desea persistir.
     * @return el mismo fiado, ya persistido.
     */
    Fiado save(Fiado fiado);

    /**
     * Busca un fiado por su ID interno (Long).
     *
     * ⚠️ En Firestore esto implica ejecutar una query where.
     * ⚠️ En SQL sería un findById tradicional.
     *
     * Se mantiene este método SOLO para compatibilidad futura.
     *
     * @param id identificador interno único del fiado.
     * @return Optional con el fiado encontrado o vacío si no existe.
     */
    Optional<Fiado> findById(Long id);

    /**
 * Busca un fiado usando el número de celular.
 *
 * 🔎 Nota importante:
 * - En nuestro modelo actual, numeroCelular NO es el ID del documento.
 * - El documento usa un `firestoreId` tipo UUID.
 * - Por eso esta operación SÍ requiere una query en Firestore.
 *
 * Este método sigue siendo necesario porque el número de celular
 * es la clave lógica del fiador en este negocio.
 */

    Optional<Fiado> findByNumeroCelular(String numeroCelular);

    /**
     * Busca uno o varios fiados por nombre del cliente.
     *
     * 🔍 Nota:
     * - Puede haber múltiples coincidencias para un mismo nombre.
     * - Por eso retorna una lista.
     *
     * En Firestore esta operación sí implica una query.
     *
     * @param nombreCliente nombre del cliente a buscar.
     * @return lista de fiados asociados a ese nombre.
     */
    List<Fiado> findByNombreCliente(String nombreCliente);

    /**
     * Recupera todos los fiados registrados en el sistema.
     *
     * ⚠️ En Firestore esto obtiene TODOS los documentos
     *    de la colección "fiados".
     *
     * @return lista completa de fiados.
     */
    List<Fiado> findAll();

    /**
     * Elimina un fiado usando el ID interno (Long).
     *
     * Como Firestore no elimina por query, este método debe:
     *  1. Buscar el documento por ID interno.
     *  2. Obtener el numeroCelular correspondiente.
     *  3. Borrar por numeroCelular.
     *
     * @param id ID interno del registro.
     */
    void deleteById(Long id);

    /**
     * Elimina un fiado usando el número de celular.
     *
     * ✅ En Firestore esta es la forma NATURAL de borrar documentos,
     * ya que numeroCelular es el ID del documento.
     *
     * @param numeroCelular número asociado al documento.
     */
    void deleteByNumeroCelular(String numeroCelular);
}
