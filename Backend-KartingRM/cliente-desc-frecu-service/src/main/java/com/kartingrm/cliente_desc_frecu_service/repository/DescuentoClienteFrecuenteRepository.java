package com.kartingrm.cliente_desc_frecu_service.repository;

import com.kartingrm.cliente_desc_frecu_service.entity.DescuentoClienteFrecuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DescuentoClienteFrecuenteRepository extends JpaRepository<DescuentoClienteFrecuente, Long> {


    @Query("SELECT d FROM DescuentoClienteFrecuente AS d WHERE :cantidad_visitas_mes BETWEEN d.minReservas AND d.maxReservas")
    Optional<DescuentoClienteFrecuente> findByVisitasBetweenMinimoMaximo(@Param("cantidad_visitas_mes") int cantidad_visitas_mes);

}
