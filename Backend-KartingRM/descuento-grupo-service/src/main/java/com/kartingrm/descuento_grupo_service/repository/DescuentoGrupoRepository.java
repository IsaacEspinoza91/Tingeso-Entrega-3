package com.kartingrm.descuento_grupo_service.repository;

import com.kartingrm.descuento_grupo_service.entity.DescuentoGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DescuentoGrupoRepository extends JpaRepository<DescuentoGrupo, Long> {

    @Query("SELECT d FROM DescuentoGrupo AS d WHERE :cantidad BETWEEN d.minPersonas AND d.maxPersonas")
    Optional<DescuentoGrupo> findByCantidadBetweenMinimoMaximo(@Param("cantidad") int cantidad);
}
