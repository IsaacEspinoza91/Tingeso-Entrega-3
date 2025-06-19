package com.kartingrm.dias_especiales_service.repository;

import com.kartingrm.dias_especiales_service.entity.DiaFeriado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaFeriadoRepository extends JpaRepository<DiaFeriado, Long> {


    // Obtiene resultado booleano si una fecha esta dentro de la tabla de dias feriados
    @Query("SELECT COUNT(*) > 0 FROM DiaFeriado df WHERE df.fecha = :fecha")
    boolean esDiaFeriado(@Param("fecha") LocalDate fecha);

    // Obtiene los dias feriados segun el anio
    @Query("SELECT df FROM DiaFeriado df WHERE EXTRACT(YEAR FROM df.fecha) = :anio")
    List<DiaFeriado> getDiasFeriadosByAnio(Integer anio);
}
