package com.kartingrm.planservice.service;

import com.kartingrm.planservice.dto.PlanDTO;
import com.kartingrm.planservice.entity.Plan;
import com.kartingrm.planservice.repository.PlanRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class PlanService {

    private PlanRepository planRepository;
    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<Plan> getPlanes(){
        return planRepository.findAll();
    }

    public Plan getPlanById(Long id){
        Optional<Plan> plan = planRepository.findById(id);
        if (plan.isEmpty()) throw new EntityNotFoundException("Plan id " + id + " no encontrado");

        return plan.get();
    }

    public Plan createPlan(PlanDTO planDTO){
        if (planDTO == null) throw new EntityNotFoundException("Plan no puede ser nulo");

        Plan plan = new Plan();
        plan.setDescripcion(planDTO.getDescripcion());
        plan.setDuracionTotal(planDTO.getDuracionTotal());
        plan.setPrecioRegular(planDTO.getPrecioRegular());
        plan.setPrecioFinSemana(planDTO.getPrecioFinSemana());
        plan.setPrecioFeriado(planDTO.getPrecioFeriado());

        return planRepository.save(plan) ;
    }

    public Plan updatePlan(Long id, PlanDTO planDTO){
        Plan planExistente = planRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Plan no encontrado"));

        planExistente.setDescripcion(planDTO.getDescripcion());
        planExistente.setDuracionTotal(planDTO.getDuracionTotal());
        planExistente.setPrecioRegular(planDTO.getPrecioRegular());
        planExistente.setPrecioFinSemana(planDTO.getPrecioFinSemana());
        planExistente.setPrecioFeriado(planDTO.getPrecioFeriado());

        return planRepository.save(planExistente);
    }

    public Boolean deletePlan(Long id){
        Optional<Plan> planOptional = planRepository.findById(id);
        if (planOptional.isEmpty()) throw new EntityNotFoundException("Plan id " + id + " no encontrado");

        planRepository.deleteById(id);
        return true;
    }
}
