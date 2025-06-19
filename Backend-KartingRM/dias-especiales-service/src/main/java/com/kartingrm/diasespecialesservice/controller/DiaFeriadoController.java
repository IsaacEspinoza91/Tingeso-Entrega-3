package com.kartingrm.diasespecialesservice.controller;

import com.kartingrm.diasespecialesservice.dto.DiaFeriadoDTO;
import com.kartingrm.diasespecialesservice.entity.DiaFeriado;
import com.kartingrm.diasespecialesservice.service.DiaFeriadoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dias-especiales-service/dias-feriados")
public class DiaFeriadoController {

    private DiaFeriadoService diaFeriadoService;
    public DiaFeriadoController(DiaFeriadoService diaFeriadoService) {
        this.diaFeriadoService = diaFeriadoService;
    }

    @GetMapping("/")
    public ResponseEntity<List<DiaFeriado>> getDiasFeriados() {
        List<DiaFeriado> dias = diaFeriadoService.getDiasFeriados();
        return ResponseEntity.ok(dias);
    }

    @GetMapping("{id}")
    public ResponseEntity<DiaFeriado> getDiasFeriadoById(@PathVariable Long id) {
        DiaFeriado dia = diaFeriadoService.getDiaFeriadoById(id);
        return ResponseEntity.ok(dia);
    }

    @PostMapping("/")
    public ResponseEntity<DiaFeriado> createDiaFeriado(@RequestBody DiaFeriadoDTO diaFeriado) {
        DiaFeriado diaFeriadoNuevo = diaFeriadoService.createDiaFeriado(diaFeriado);
        return ResponseEntity.ok(diaFeriadoNuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaFeriado> updateDiaFeriado(@PathVariable Long id, @RequestBody DiaFeriadoDTO diaFeriado) {
        DiaFeriado diaActualizado = diaFeriadoService.updateDiaFeriado(id, diaFeriado);
        return ResponseEntity.ok(diaActualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteDiaFeriado(@PathVariable Long id) {
        diaFeriadoService.deleteDiaFeriado(id);
        return ResponseEntity.noContent().build();
    }

    // Obtiene booleano que indica si dia es feriado o no.
    //  Utilizar formato:      fecha=YYYY-MM-DD
    @GetMapping("/esFeriado")
    public ResponseEntity<Boolean> esFeriado(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        boolean esFeriado = diaFeriadoService.esDiaFeriado(fecha);
        return ResponseEntity.ok(esFeriado);
    }

    // Obtiene dias feriados segun anio
    @GetMapping("/anio/{numeroAnio}")
    public ResponseEntity<List<DiaFeriado>> getDiasFeriadosByAnio(@PathVariable Integer numeroAnio) {
        List<DiaFeriado> diasFeriadosEnAnio = diaFeriadoService.getDiasFeriadosByAnio(numeroAnio);
        return ResponseEntity.ok(diasFeriadosEnAnio);
    }
}
