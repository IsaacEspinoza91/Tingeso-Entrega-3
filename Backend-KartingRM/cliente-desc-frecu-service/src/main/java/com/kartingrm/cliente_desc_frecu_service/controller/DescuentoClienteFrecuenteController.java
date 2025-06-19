package com.kartingrm.cliente_desc_frecu_service.controller;

import com.kartingrm.cliente_desc_frecu_service.entity.DescuentoClienteFrecuente;
import com.kartingrm.cliente_desc_frecu_service.service.DescuentoClienteFrecuenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente-service/descuento-frecuente")
public class DescuentoClienteFrecuenteController {

    @Autowired
    private DescuentoClienteFrecuenteService descuentoClienteFrecuenteService;

    @GetMapping("/")
    public ResponseEntity<List<DescuentoClienteFrecuente>> getDescuentosClienteFrecuente() {
        List<DescuentoClienteFrecuente> descuentos = descuentoClienteFrecuenteService.getDescuentosClienteFrecuente();
        return ResponseEntity.ok().body(descuentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DescuentoClienteFrecuente> getDescuentoClienteFrecuenteById(@PathVariable Long id) {
        DescuentoClienteFrecuente desc = descuentoClienteFrecuenteService.getDescuentoClienteFrecuenteById(id);
        return ResponseEntity.ok().body(desc);
    }

    @PostMapping("/")
    public ResponseEntity<DescuentoClienteFrecuente> createDescuentoClienteFrecuente(@RequestBody DescuentoClienteFrecuente descuento) {
        DescuentoClienteFrecuente descuentoCreado = descuentoClienteFrecuenteService.createDescuentoClienteFrecuente(descuento);
        return ResponseEntity.ok().body(descuentoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DescuentoClienteFrecuente> updateDescuentoClienteFrecuente(@PathVariable Long id, @RequestBody DescuentoClienteFrecuente descuento) {
        DescuentoClienteFrecuente descuentoActualizado = descuentoClienteFrecuenteService.updateDescuentoClienteFrecuente(id, descuento);
        return ResponseEntity.ok().body(descuentoActualizado);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDescuentoClienteFrecuente(@PathVariable Long id) {
        descuentoClienteFrecuenteService.deleteDescuentoClienteFrecuente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/visitas/{cantidadvisitas}")
    public ResponseEntity<Double> getPorcentajeDescuentoGrupoByCantidadIntegrantes(@PathVariable Integer cantidadvisitas) {
        double porcentaje = descuentoClienteFrecuenteService.getPorcentajeDescuentoClienteFrecuenteByCantidadVisitas(cantidadvisitas);
        return ResponseEntity.ok().body(porcentaje);
    }

}
