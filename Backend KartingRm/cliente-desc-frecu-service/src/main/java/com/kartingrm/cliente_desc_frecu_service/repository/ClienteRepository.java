package com.kartingrm.cliente_desc_frecu_service.repository;

import com.kartingrm.cliente_desc_frecu_service.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findClientesByActivo(boolean activo);

    Optional<Cliente> findClienteByRut(String rut);

    Optional<List<Cliente>> findClienteByNombreAndApellido(String nombre, String apellido);
}
