package com.kartingrm.plan_service.service;

import com.kartingrm.plan_service.entity.Plan;
import com.kartingrm.plan_service.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<Plan> getPlanes(){
        return planRepository.findAll();
    }

    public Plan getPlanById(Long id){
        Optional<Plan> plan = planRepository.findById(id);
        if (plan.isEmpty()) throw new EntityNotFoundException("Plan id " + id + " no encontrado");

        return plan.get();
    }

    public Plan createPlan(Plan plan){
        return planRepository.save(plan);
    }

    public Plan updatePlan(Long id, Plan plan){
        Optional<Plan> planOptional = planRepository.findById(id);
        if (planOptional.isEmpty()) throw new EntityNotFoundException("Plan id " + id + " no encontrado");

        plan.setId(id);
        return planRepository.save(plan);
    }

    public Boolean deletePlan(Long id){
        Optional<Plan> planOptional = planRepository.findById(id);
        if (planOptional.isEmpty()) throw new EntityNotFoundException("Plan id " + id + " no encontrado");

        planRepository.deleteById(id);
        return true;
    }
}
