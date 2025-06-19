package com.kartingrm.rack_semanal_service.repository;

import com.kartingrm.rack_semanal_service.entity.RackReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RackReservaRepository extends JpaRepository<RackReserva, Long> {

    // Obtener todos los elementos reserva en la tabla rack_semana entre dos fechas de inicio y fin de semana
    List<RackReserva> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}