package com.kartingrm.reportes_service.repository;

import com.kartingrm.reportes_service.entity.ReporteIngresosPersonas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteIngresosPersonasRepository extends JpaRepository<ReporteIngresosPersonas, Long> {
    Optional<ReporteIngresosPersonas> findByRangoPersonasAndMes(String rangoPersonas, LocalDate mes);

    @Modifying
    @Query("UPDATE ReporteIngresosPersonas r SET r.ingresos = r.ingresos + :monto WHERE r.rangoPersonas = :rango AND r.mes = :mes")
    void sumarIngresos(@Param("rango") String rangoPersonas, @Param("mes") LocalDate mes, @Param("monto") Double monto);

    @Modifying
    @Query("UPDATE ReporteIngresosPersonas r SET r.ingresos = r.ingresos - :monto WHERE r.rangoPersonas = :rango AND r.mes = :mes")
    void restarIngresos(@Param("rango") String rangoPersonas, @Param("mes") LocalDate mes, @Param("monto") Double monto);

    @Query("SELECT r FROM ReporteIngresosPersonas r " +
            "WHERE r.mes BETWEEN :mesInicio AND :mesFin " +
            "ORDER BY r.rangoPersonas, r.mes")
    List<ReporteIngresosPersonas> findReportesPersonasEntreMeses(@Param("mesInicio") LocalDate mesInicio, @Param("mesFin") LocalDate mesFin);
}