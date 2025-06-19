package com.kartingrm.plan_service.controller;

import com.kartingrm.plan_service.entity.Plan;
import com.kartingrm.plan_service.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plan/planes")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("/")
    public ResponseEntity<List<Plan>> getPlanes(){
        List<Plan> planEntities = planService.getPlanes();
        return ResponseEntity.ok(planEntities);
    }

    @GetMapping("/{id_plan}")
    public ResponseEntity<Plan> getPlanById(@PathVariable Long id_plan){
        Plan plan = planService.getPlanById(id_plan);
        return ResponseEntity.ok(plan);
    }

    @PostMapping("/")
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan){
        Plan planNuevo = planService.createPlan(plan);
        return ResponseEntity.ok(planNuevo);
    }

    @PutMapping("/{id_plan}")
    public ResponseEntity<Plan> updatePlan(@PathVariable Long id_plan, @RequestBody Plan plan){
        Plan planActualizado = planService.updatePlan(id_plan, plan);
        return ResponseEntity.ok(planActualizado);
    }

    @DeleteMapping("/{id_plan}")
    public ResponseEntity<Boolean> deletePlan(@PathVariable Long id_plan) {
        planService.deletePlan(id_plan);
        return ResponseEntity.noContent().build();
    }

}
