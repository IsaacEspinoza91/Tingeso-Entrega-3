package com.kartingrm.reservas_comprobantes_service.repository;

import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findReservasByIdReservante(Long idReservante);

    // Obtiene las reservas existentes entre dos horas de un mismo dia
    // Regla de negocio: no se pueden hacer reservas en un horario donde la pista este reservada
    @Query("SELECT COUNT(*) > 0 " +
            "FROM Reserva r " +
            "WHERE r.fecha = :fecha AND (:horaInicio < r.horaFin AND :horaFin > r.horaInicio) AND r.estado = 'confirmada'")
    boolean existeReservaEntreDosHoras(@Param("fecha") LocalDate fecha,
                                                @Param("horaInicio") LocalTime horaInicio,
                                                @Param("horaFin") LocalTime horaFin);

}
