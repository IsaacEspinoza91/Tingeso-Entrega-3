package com.kartingrm.descuento_grupo_service.controller;

import com.kartingrm.descuento_grupo_service.entity.DescuentoGrupo;
import com.kartingrm.descuento_grupo_service.service.DescuentoGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/descuento-grupo-service/desc-grupo")
public class DescuentoGrupoController {

    @Autowired
    private DescuentoGrupoService descuentoGrupoService;


    @GetMapping("/")
    public ResponseEntity<List<DescuentoGrupo>> getDescuentosGrupos(){
        List<DescuentoGrupo> descuentoGrupo = descuentoGrupoService.getDescuentosGrupos();
        return ResponseEntity.ok(descuentoGrupo);
    }

    @GetMapping("/{id_desc}")
    public ResponseEntity<DescuentoGrupo> getDescuentoGrupoById(@PathVariable Long id_desc){
        DescuentoGrupo descuentoGrupo = descuentoGrupoService.getDescuentoGrupoById(id_desc);
        return ResponseEntity.ok(descuentoGrupo);
    }

    @PostMapping("/")
    public ResponseEntity<DescuentoGrupo> createDescuentoGrupo(@RequestBody DescuentoGrupo descuentoGrupo){
        DescuentoGrupo planNuevo = descuentoGrupoService.createDescuentoGrupo(descuentoGrupo);
        return ResponseEntity.ok(planNuevo);
    }

    @PutMapping("/{id_desc}")
    public ResponseEntity<DescuentoGrupo> updateDescuentoGrupo(@PathVariable Long id_desc, @RequestBody DescuentoGrupo descuentoGrupo){
        DescuentoGrupo descuentoActualizado = descuentoGrupoService.updateDescuentoGrupo(id_desc, descuentoGrupo);
        return ResponseEntity.ok(descuentoActualizado);
    }

    @DeleteMapping("/{id_desc}")
    public ResponseEntity<Boolean> deleteDescuentoGrupo(@PathVariable Long id_desc) {
        descuentoGrupoService.deleteDescuentoGrupo(id_desc);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cantidad/{cantidadPersonas}")
    public ResponseEntity<Double> getPorcentajeDescuentoGrupoByCantidadIntegrantes(@PathVariable Integer cantidadPersonas){
        double porcentaje = descuentoGrupoService.getPorcentajeDescuentoGrupoByCantidadIntegrantes(cantidadPersonas);
        return ResponseEntity.ok(porcentaje);
    }
}
