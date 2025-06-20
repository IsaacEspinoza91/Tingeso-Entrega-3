package com.kartingrm.rack_semanal_service.controller;

import com.kartingrm.rack_semanal_service.dto.ReservaSemanalResponse;
import com.kartingrm.rack_semanal_service.service.RackSemanalService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rack-semanal-service/get-rack")
public class RackSemanalController {

    private RackSemanalService rackSemanalService;
    public RackSemanalController(RackSemanalService rackSemanalService) {
        this.rackSemanalService = rackSemanalService;
    }

    @GetMapping("/{semana}")
    public ReservaSemanalResponse obtenerRackSemanal(@PathVariable int semana) {
        return rackSemanalService.obtenerReservasSemana(semana);
    }

}