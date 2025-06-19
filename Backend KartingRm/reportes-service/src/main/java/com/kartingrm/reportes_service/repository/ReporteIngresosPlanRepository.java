package com.kartingrm.reportes_service.repository;

import com.kartingrm.reportes_service.entity.ReporteIngresosPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteIngresosPlanRepository extends JpaRepository<ReporteIngresosPlan, Long> {
    Optional<ReporteIngresosPlan> findByIdPlanAndMes(Long idPlan, LocalDate mes);

    @Modifying
    @Query("UPDATE ReporteIngresosPlan r SET r.ingresos = r.ingresos + :monto WHERE r.idPlan = :idPlan AND r.mes = :mes")
    void sumarIngresos(@Param("idPlan") Long idPlan, @Param("mes") LocalDate mes, @Param("monto") Double monto);

    @Modifying
    @Query("UPDATE ReporteIngresosPlan r SET r.ingresos = r.ingresos - :monto WHERE r.idPlan = :idPlan AND r.mes = :mes")
    void restarIngresos(@Param("idPlan") Long idPlan, @Param("mes") LocalDate mes, @Param("monto") Double monto);

    @Query("SELECT r FROM ReporteIngresosPlan r WHERE r.mes BETWEEN :mesInicio AND :mesFin ORDER BY r.descripcionPlan, r.mes")
    List<ReporteIngresosPlan> findReportesPlanEntreMeses(@Param("mesInicio") LocalDate mesInicio, @Param("mesFin") LocalDate mesFin);
}