package com.kartingrm.reservas_comprobantes_service.controller;

import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.ReservaCreateRequest;
import com.kartingrm.reservas_comprobantes_service.model.ReservaDTO;
import com.kartingrm.reservas_comprobantes_service.model.ReservaRequest;
import com.kartingrm.reservas_comprobantes_service.model.ReservasDiariasDTO;
import com.kartingrm.reservas_comprobantes_service.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas-comprobantes-service/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }


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

    @GetMapping("/DTO/busqueda-nombre/{nombre}")
    public ResponseEntity<List<ReservaDTO>> getReservasDTOByBusquedaParcialNombre(@PathVariable String nombre) {
        List<ReservaDTO> reservas = reservaService.getReservasDTOByNombreParcialCliente(nombre);
        return ResponseEntity.ok(reservas);
    }

    @PostMapping("/")
    public ResponseEntity<Reserva> createReserva(@RequestBody ReservaRequest reserva) {
        try{
            Reserva reservaNuevo = reservaService.createReserva(reserva);
            return ResponseEntity.ok(reservaNuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/crear-completa/")
    public ResponseEntity<String> createReservaCompleta(@RequestBody ReservaCreateRequest request) {
        try {
            reservaService.createReservaCompleta(request);
            return ResponseEntity.ok("Reserva creada exitosamente.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Long id, @RequestBody ReservaRequest reserva) {
        try {
            Reserva reservaActualizada = reservaService.updateReserva(id, reserva);
            return ResponseEntity.ok(reservaActualizada);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update-completa/{idReserva}")
    public ResponseEntity<String> updateReservaCompleta(@PathVariable Long idReserva, @RequestBody ReservaCreateRequest request) {
        try {
            reservaService.updateReservaCompleta(idReserva, request);
            return ResponseEntity.ok("Reserva actualizada exitosamente.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        reservaService.deleteReserva(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contador-diario")
    public ReservasDiariasDTO getContadorReservasDiarias() {
        return reservaService.contarReservasDiarias();
    }


}
