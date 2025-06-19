package com.kartingrm.descuento_grupo_service.controller;

import com.kartingrm.descuento_grupo_service.dto.DescuentoGrupoDTO;
import com.kartingrm.descuento_grupo_service.entity.DescuentoGrupo;
import com.kartingrm.descuento_grupo_service.service.DescuentoGrupoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/descuento-grupo-service/desc-grupo")
public class DescuentoGrupoController {

    private DescuentoGrupoService descuentoGrupoService;
    public DescuentoGrupoController(DescuentoGrupoService descuentoGrupoService) {
        this.descuentoGrupoService = descuentoGrupoService;
    }

    @GetMapping("/")
    public ResponseEntity<List<DescuentoGrupo>> getDescuentosGrupos(){
        List<DescuentoGrupo> descuentoGrupo = descuentoGrupoService.getDescuentosGrupos();
        return ResponseEntity.ok(descuentoGrupo);
    }

    @GetMapping("/{idDesc}")
    public ResponseEntity<DescuentoGrupo> getDescuentoGrupoById(@PathVariable Long idDesc){
        DescuentoGrupo descuentoGrupo = descuentoGrupoService.getDescuentoGrupoById(idDesc);
        return ResponseEntity.ok(descuentoGrupo);
    }

    @PostMapping("/")
    public ResponseEntity<DescuentoGrupo> createDescuentoGrupo(@RequestBody DescuentoGrupoDTO descuentoGrupo){
        DescuentoGrupo planNuevo = descuentoGrupoService.createDescuentoGrupo(descuentoGrupo);
        return ResponseEntity.ok(planNuevo);
    }

    @PutMapping("/{idDesc}")
    public ResponseEntity<DescuentoGrupo> updateDescuentoGrupo(@PathVariable Long idDesc, @RequestBody DescuentoGrupoDTO descuentoGrupo){
        DescuentoGrupo descuentoActualizado = descuentoGrupoService.updateDescuentoGrupo(idDesc, descuentoGrupo);
        return ResponseEntity.ok(descuentoActualizado);
    }

    @DeleteMapping("/{idDesc}")
    public ResponseEntity<Boolean> deleteDescuentoGrupo(@PathVariable Long idDesc) {
        descuentoGrupoService.deleteDescuentoGrupo(idDesc);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cantidad/{cantidadPersonas}")
    public ResponseEntity<Double> getPorcentajeDescuentoGrupoByCantidadIntegrantes(@PathVariable Integer cantidadPersonas){
        double porcentaje = descuentoGrupoService.getPorcentajeDescuentoGrupoByCantidadIntegrantes(cantidadPersonas);
        return ResponseEntity.ok(porcentaje);
    }
}
