package com.kartingrm.cliente_desc_frecu_service.repository;

import com.kartingrm.cliente_desc_frecu_service.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findClientesByActivo(boolean activo);

    Optional<Cliente> findClienteByRut(String rut);

    Optional<List<Cliente>> findClienteByNombreAndApellido(String nombre, String apellido);

    @Query("SELECT c FROM Cliente c WHERE " +
            "LOWER(CONCAT(c.nombre, ' ', c.apellido)) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(CONCAT(c.apellido, ' ', c.nombre)) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Cliente> buscarPorNombreApellidoParcial(@Param("busqueda") String busqueda);

    @Query("SELECT CAST(c.id AS long) FROM Cliente c WHERE " +
            "LOWER(CONCAT(c.nombre, ' ', c.apellido)) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(CONCAT(c.apellido, ' ', c.nombre)) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Long> findIdClientebuscarPorNombreApellidoParcial(@Param("busqueda") String busqueda);


    @Query("SELECT c FROM Cliente c WHERE " +
            "REPLACE(SUBSTRING_INDEX(c.rut, '-', 1), '.', '') LIKE " +
            "CONCAT(REPLACE(SUBSTRING_INDEX(REPLACE(:rut, '.', ''), '-', 1), '.', ''), '%')")
    List<Cliente> findClientesByRutParcial(@Param("rut") String rut);

}
