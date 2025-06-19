package com.kartingrm.dias_especiales_service.repository;

import com.kartingrm.dias_especiales_service.entity.ClienteCumpleanios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ClienteCumpleaniosRepository extends JpaRepository<ClienteCumpleanios, Long> {

    // Obtiene bool sobre si cliente esta de cumpleanos, recordar comparacion de mes y dia, no anio
    @Query("SELECT COUNT(*) > 0 FROM ClienteCumpleanios cc " +
            "WHERE cc.idCliente = :idCliente " +
            "AND EXTRACT(DAY FROM cc.fecha) = EXTRACT(DAY FROM :fecha) " +
            "AND EXTRACT(MONTH FROM cc.fecha) = EXTRACT(MONTH FROM :fecha)")
    boolean estaDeCumpleanios(@Param("idCliente") Long idCliente, @Param("fecha") LocalDate fecha);
}
