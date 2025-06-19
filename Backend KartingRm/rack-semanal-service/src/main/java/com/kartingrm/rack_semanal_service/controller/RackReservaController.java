package com.kartingrm.rack_semanal_service.controller;

import com.kartingrm.rack_semanal_service.entity.RackReserva;
import com.kartingrm.rack_semanal_service.service.RackReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rack-semanal-service/rack-reserva")
public class RackReservaController {

    @Autowired
    private RackReservaService rackReservaService;

    @GetMapping("/")
    public ResponseEntity<List<RackReserva>> getRackReservas() {
        List<RackReserva> rackReservas = rackReservaService.getRackReservas();
        return ResponseEntity.ok(rackReservas);
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity<RackReserva> getRackReservaById(@PathVariable("idReserva") Long idReserva) {
        RackReserva rack = rackReservaService.getRackReservaById(idReserva);
        return ResponseEntity.ok(rack);
    }

    @PostMapping("/")
    public ResponseEntity<RackReserva> createRackReserva(@RequestBody RackReserva rackReserva) {
        try {
            RackReserva rack = rackReservaService.createRackReserva(rackReserva);
            return ResponseEntity.ok(rack);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{idReserva}")
    public ResponseEntity<RackReserva> updateRackReserva(@PathVariable("idReserva") Long idReserva, @RequestBody RackReserva rackReserva) {
        try {
            RackReserva rack = rackReservaService.updateRackReserva(idReserva,rackReserva);
            return ResponseEntity.ok(rack);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{idReserva}")
    public ResponseEntity<Void> deleteRackReserva(@PathVariable("idReserva") Long idReserva) {
        rackReservaService.deleteRackReservaById(idReserva);
        return ResponseEntity.noContent().build();
    }

}