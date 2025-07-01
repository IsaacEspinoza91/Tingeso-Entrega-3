package com.kartingrm.planservice.controller;

import com.kartingrm.planservice.dto.PlanDTO;
import com.kartingrm.planservice.entity.Plan;
import com.kartingrm.planservice.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plan/planes")
public class PlanController {

    private final PlanService planService;
    public PlanController(PlanService planService) {
        this.planService = planService;
    }


    @GetMapping("/")
    public ResponseEntity<List<Plan>> getPlanes(){
        List<Plan> planEntities = planService.getPlanes();
        return ResponseEntity.ok(planEntities);
    }

    @GetMapping("/buscar/{texto}")
    public ResponseEntity<List<Plan>> getPlanesBuscados(@PathVariable("texto") String texto){
        List<Plan> planes = planService.getPlanesByBusqueda(texto);
        return ResponseEntity.ok(planes);
    }

    @GetMapping("/{idPlan}")
    public ResponseEntity<Plan> getPlanById(@PathVariable Long idPlan){
        Plan plan = planService.getPlanById(idPlan);
        return ResponseEntity.ok(plan);
    }

    @PostMapping("/")
    public ResponseEntity<Plan> createPlan(@RequestBody PlanDTO plan){
        Plan planNuevo = planService.createPlan(plan);
        return ResponseEntity.ok(planNuevo);
    }

    @PutMapping("/{idPlan}")
    public ResponseEntity<Plan> updatePlan(@PathVariable Long idPlan, @RequestBody PlanDTO plan){
        Plan planActualizado = planService.updatePlan(idPlan, plan);
        return ResponseEntity.ok(planActualizado);
    }

    @DeleteMapping("/{idPlan}")
    public ResponseEntity<Boolean> deletePlan(@PathVariable Long idPlan) {
        planService.deletePlan(idPlan);
        return ResponseEntity.noContent().build();
    }

}
