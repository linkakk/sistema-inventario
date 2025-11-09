package com.panaderia.fiados.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.panaderia.fiados.model.Fiado;

/**
 * ============================================================
 * 📂 Repositorio: FiadoRepository
 * ============================================================
 *
 * Interfaz encargada de la comunicación directa con la base de datos.
 *
 * Extiende de {@link JpaRepository}, lo cual proporciona automáticamente
 * un conjunto de operaciones CRUD (Create, Read, Update, Delete)
 * sobre la entidad {@link Fiado}.
 *
 * Su responsabilidad es **persistir y recuperar datos** sin preocuparse
 * por la lógica de negocio.
 *
 * Spring Data JPA se encarga de generar las implementaciones concretas
 * en tiempo de ejecución, por lo que no es necesario escribir código SQL
 * manualmente.
 */
@Repository
public interface FiadoRepository extends JpaRepository<Fiado, Long> {

    /**
     * Busca un registro de fiado por el nombre del cliente.
     *
     * Spring genera automáticamente la consulta a partir del nombre
     * del método siguiendo la convención "findBy...".
     *
     * @param nombreCliente nombre del cliente a buscar.
     * @return un {@link Optional} que puede contener el fiado si existe,
     *         o estar vacío si no se encontró coincidencia.
     */
    Optional<Fiado> findByNombreCliente(String nombreCliente);

    // 🧩 Ejemplo de futuras consultas personalizadas:
    // Optional<Fiado> findByNumeroCelular(String numeroCelular);
    // List<Fiado> findByActivoTrue();
}
