package com.kartingrm.reservas_comprobantes_service.repository;

import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.ReservasDiariasDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findReservasByIdReservante(Long idReservante);

    // Obtiene las reservas existentes entre dos horas de un mismo dia
    // Regla de negocio: no se pueden hacer reservas en un horario donde la pista este reservada
    @Query("SELECT COUNT(*) > 0 " +
            "FROM Reserva r " +
            "WHERE r.fecha = :fecha AND (:horaInicio < r.horaFin AND :horaFin > r.horaInicio) AND r.estado = 'confirmada'")
    boolean existeReservaEntreDosHoras(@Param("fecha") LocalDate fecha, @Param("horaInicio") LocalTime horaInicio, @Param("horaFin") LocalTime horaFin);


    @Query("SELECT r " +
            "FROM Reserva r " +
            "WHERE r.fecha = :fecha AND (:horaInicio < r.horaFin AND :horaFin > r.horaInicio) AND r.estado = 'confirmada'")
    Optional<Reserva> encontrarReservaEntreDosHoras(@Param("fecha") LocalDate fecha, @Param("horaInicio") LocalTime horaInicio, @Param("horaFin") LocalTime horaFin);


    @Query("SELECT " +
            "new com.kartingrm.reservas_comprobantes_service.model.ReservasDiariasDTO(" +
            "COUNT(CASE WHEN r.fecha = :fechaHoy AND (r.estado = 'CONFIRMADA' OR r.estado = 'COMPLETADA') THEN 1 END), " +
            "COUNT(CASE WHEN r.fecha = :fechaAyer AND (r.estado = 'CONFIRMADA' OR r.estado = 'COMPLETADA') THEN 1 END)) " +
            "FROM Reserva r")
    ReservasDiariasDTO countReservasConfirmadasOCompletadasDiarias(@Param("fechaHoy") LocalDate fechaHoy, @Param("fechaAyer") LocalDate fechaAyer);

    List<Reserva> findByFecha(LocalDate fecha);
}
