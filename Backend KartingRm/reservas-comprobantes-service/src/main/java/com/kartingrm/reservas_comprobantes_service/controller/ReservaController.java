package com.kartingrm.reservas_comprobantes_service.controller;

import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.ReservaDTO;
import com.kartingrm.reservas_comprobantes_service.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas-comprobantes-service/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;


    @GetMapping("/")
    public ResponseEntity<List<Reserva>> getReservas() {
        List<Reserva> reservas =  reservaService.getReservas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/DTO/")
    public ResponseEntity<List<ReservaDTO>> getReservasDTO() {
        List<ReservaDTO> reservaDTOS = reservaService.getReservasDTO();
        return ResponseEntity.ok(reservaDTOS);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Long id) {
        Reserva reserva =  reservaService.getReservaById(id);
        return ResponseEntity.ok(reserva);
    }

    @GetMapping ("/DTO/{id}")
    public ResponseEntity<ReservaDTO> getReservaDTOById(@PathVariable Long id) {
        ReservaDTO reserva =  reservaService.getReservaDTOById(id);
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/reservante/{idCliente}")
    public ResponseEntity<List<Reserva>> getReservasByIdReservante(@PathVariable Long idCliente) {
        List<Reserva> reservas = reservaService.getReservasByIdReservante(idCliente);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/DTO/reservante/{idCliente}")
    public ResponseEntity<List<ReservaDTO>> getReservasDTOByIdReservante(@PathVariable Long idCliente) {
        List<ReservaDTO> reservas = reservaService.getReservasDTOByIdReservante(idCliente);
        return ResponseEntity.ok(reservas);
    }


    @PostMapping("/")
    public ResponseEntity<Reserva> createReserva(@RequestBody Reserva reserva) {
        try{
            Reserva reservaNuevo = reservaService.createReserva(reserva);
            return ResponseEntity.ok(reservaNuevo);
        } catch (Exception e) {
            // Printear error en la cosola backend
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Long id, @RequestBody Reserva reserva) {
        try {
            Reserva reservaActualizada = reservaService.updateReserva(id, reserva);
            return ResponseEntity.ok(reservaActualizada);
        } catch (Exception e){
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        reservaService.deleteReserva(id);
        return ResponseEntity.noContent().build();
    }

}
