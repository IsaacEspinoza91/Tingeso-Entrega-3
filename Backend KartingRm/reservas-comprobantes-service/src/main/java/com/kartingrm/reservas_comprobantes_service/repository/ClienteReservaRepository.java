package com.kartingrm.reservas_comprobantes_service.repository;

import com.kartingrm.reservas_comprobantes_service.entity.ClienteReserva;
import com.kartingrm.reservas_comprobantes_service.entity.ClienteReservaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteReservaRepository extends JpaRepository<ClienteReserva, ClienteReservaId> {

    List<ClienteReserva> findByIdReserva(Long idReserva);

    int countByIdReserva(Long idReserva);

    boolean existsByIdClienteAndIdReserva(Long idCliente, Long idReserva);
}