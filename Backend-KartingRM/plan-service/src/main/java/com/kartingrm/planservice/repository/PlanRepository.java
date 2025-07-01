package com.kartingrm.planservice.repository;

import com.kartingrm.planservice.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    // Buscar planes cuya descripción contenga el texto buscado (mayúscula o minúscula)
    List<Plan> findByDescripcionContainingIgnoreCase(String textoBusqueda);
}