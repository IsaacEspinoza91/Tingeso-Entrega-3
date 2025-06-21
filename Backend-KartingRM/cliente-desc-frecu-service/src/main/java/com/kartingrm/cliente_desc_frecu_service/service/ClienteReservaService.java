package com.kartingrm.cliente_desc_frecu_service.service;

import com.kartingrm.cliente_desc_frecu_service.dto.ClienteReservaDTO;
import com.kartingrm.cliente_desc_frecu_service.entity.ClienteReserva;
import com.kartingrm.cliente_desc_frecu_service.entity.ClienteReservaId;
import com.kartingrm.cliente_desc_frecu_service.repository.ClienteReservaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteReservaService {

    private ClienteReservaRepository clienteReservaRepository;
    private DescuentoClienteFrecuenteService descuentoClienteFrecuenteService;
    public ClienteReservaService(ClienteReservaRepository clienteReservaRepository,DescuentoClienteFrecuenteService descuentoClienteFrecuenteService) {
        this.clienteReservaRepository = clienteReservaRepository;
        this.descuentoClienteFrecuenteService = descuentoClienteFrecuenteService;
    }


    public List<ClienteReserva> getClientesReservas() {
        return clienteReservaRepository.findAll();
    }

    public ClienteReserva getClienteReservaById(ClienteReservaId id) {
        Optional<ClienteReserva> clienteReserva = clienteReservaRepository.findById(id);
        if (clienteReserva.isEmpty()) throw new EntityNotFoundException("ClienteReserva no encontrado");

        return clienteReserva.get();
    }

    public List<ClienteReserva> obtenerReservasPorClienteId(Long idCliente) {
        return clienteReservaRepository.findById_IdCliente(idCliente);
    }

    public ClienteReserva createClienteReserva(ClienteReservaDTO reservaDTO) {
        if (reservaDTO == null) {
            throw new EntityNotFoundException("Reserva no puede ser nula");
        }
        if (reservaDTO.getId() == null || reservaDTO.getId().getIdReserva() == null || reservaDTO.getId().getIdCliente() == null) {
            throw new IllegalArgumentException("El ID de reserva y el ID de cliente son obligatorios.");
        }
        if (clienteReservaRepository.existsById(reservaDTO.getId())) {
            throw new IllegalArgumentException("Ya existe una reserva con el ID reserva " + reservaDTO.getId().getIdReserva() +
                    " e ID cliente " + reservaDTO.getId().getIdCliente());
        }

        ClienteReserva reserva = new ClienteReserva();
        reserva.setEstado(reservaDTO.getEstado());
        reserva.setFecha(reservaDTO.getFecha());
        reserva.setId(new ClienteReservaId(reservaDTO.getId().getIdReserva(), reservaDTO.getId().getIdCliente()));

        if (reservaDTO.getEstado() == null) reserva.setEstado("completada");
        return clienteReservaRepository.save(reserva);
    }

    public ClienteReserva updateClienteReserva(ClienteReservaId id, ClienteReservaDTO cliReser) {
        ClienteReserva clienteReservaExistente = clienteReservaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ClienteReserva no encontrado"));

        clienteReservaExistente.setEstado(cliReser.getEstado());
        clienteReservaExistente.setFecha(cliReser.getFecha());

        return clienteReservaRepository.save(clienteReservaExistente);
    }

    public Boolean deleteClienteReserva(ClienteReservaId id) {
        Optional<ClienteReserva> clienteReserva = clienteReservaRepository.findById(id);
        if (clienteReserva.isEmpty()) throw new EntityNotFoundException("ClienteReserva no encontrado");

        clienteReservaRepository.deleteById(id);
        return true;
    }

    // Obtiene la cantidad de veces que un cliente según id ha participado en una reserva, sea comprador o integrante
    public int getReservasCompletadasUltimoMes(Long idCliente) {
        LocalDate fechaInicio = LocalDate.now().minusDays(30);
        return clienteReservaRepository.countReservasCompletadasDespuesDeFecha(idCliente, fechaInicio);
    }

    // Obtiene el descuento según reglas de cliente frecuente según id de cliente
    public double getDescuentoClienteFrecuenteSegunIdCliente(Long idCliente) {
        int cantidadVisitasUltimoMes = getReservasCompletadasUltimoMes(idCliente);
        return descuentoClienteFrecuenteService.getPorcentajeDescuentoClienteFrecuenteByCantidadVisitas(cantidadVisitasUltimoMes);
    }

}
