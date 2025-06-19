package com.kartingrm.rack_semanal_service.controller;

import com.kartingrm.rack_semanal_service.DTO.ReservaSemanalResponse;
import com.kartingrm.rack_semanal_service.service.RackSemanalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rack-semanal-service/get-rack")
public class RackSemanalController {

    @Autowired
    private RackSemanalService rackSemanalService;

    @GetMapping("/{semana}")
    public ReservaSemanalResponse obtenerRackSemanal(@PathVariable int semana) {
        return rackSemanalService.obtenerReservasSemana(semana);
    }

}