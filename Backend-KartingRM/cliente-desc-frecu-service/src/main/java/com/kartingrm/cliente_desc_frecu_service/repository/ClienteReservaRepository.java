package com.kartingrm.cliente_desc_frecu_service.repository;

import com.kartingrm.cliente_desc_frecu_service.entity.ClienteReserva;
import com.kartingrm.cliente_desc_frecu_service.entity.ClienteReservaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClienteReservaRepository extends JpaRepository<ClienteReserva, ClienteReservaId> {

    // Encuentra por idCliente dentro del ID compuesto
    List<ClienteReserva> findById_IdCliente(Long idCliente);

    // Cuenta la cantidad de visitas de un cliente en los ultimos 30 dias desde el dia actual
    @Query("SELECT COUNT(*) FROM ClienteReserva cr " +
            "WHERE cr.id.idCliente = :idCliente " +
            "AND cr.estado = 'completada' " +
            "AND cr.fecha >= :fechaInicio")
    int countReservasCompletadasDespuesDeFecha(@Param("idCliente") Long idCliente, @Param("fechaInicio") LocalDate fechaInicio);

}