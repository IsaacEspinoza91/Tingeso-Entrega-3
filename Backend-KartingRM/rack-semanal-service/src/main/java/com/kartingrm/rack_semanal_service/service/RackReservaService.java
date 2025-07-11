package com.kartingrm.rack_semanal_service.service;

import com.kartingrm.rack_semanal_service.dto.RackReservaDTO;
import com.kartingrm.rack_semanal_service.entity.RackReserva;
import com.kartingrm.rack_semanal_service.repository.RackReservaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class RackReservaService {

    private RackReservaRepository rackReservaRepository;
    public RackReservaService(RackReservaRepository rackReservaRepository) {
        this.rackReservaRepository = rackReservaRepository;
    }

    public List<RackReserva> getRackReservas() {
        return rackReservaRepository.findAll();
    }

    public RackReserva getRackReservaById(Long id) {
        Optional<RackReserva> rackReserva = rackReservaRepository.findById(id);
        if (rackReserva.isEmpty()) throw new EntityNotFoundException("Rack Reserva no encontrado");

        return rackReserva.get();
    }

    public RackReserva createRackReserva(RackReservaDTO rackReservaDTO) {
        if (rackReservaDTO == null) throw new EntityNotFoundException("Rack Reserva no encontrado");

        RackReserva rackReserva = new RackReserva();
        rackReserva.setFecha(rackReservaDTO.getFecha());
        rackReserva.setHoraFin(rackReservaDTO.getHoraFin());
        rackReserva.setHoraInicio(rackReservaDTO.getHoraInicio());
        rackReserva.setNombreReservante(rackReservaDTO.getNombreReservante());
        rackReserva.setIdCliente(rackReservaDTO.getIdCliente());
        rackReserva.setIdReserva(rackReservaDTO.getIdReserva());

        return rackReservaRepository.save(rackReserva);
    }

    public RackReserva updateRackReserva(Long idReserva, RackReservaDTO rackReservaDTO) {
        RackReserva rackExistente = rackReservaRepository.findRackReservaByIdReserva(idReserva);
        if (rackExistente == null) throw new EntityNotFoundException("Rack Reserva no encontrado");

        rackExistente.setFecha(rackReservaDTO.getFecha());
        rackExistente.setHoraFin(rackReservaDTO.getHoraFin());
        rackExistente.setHoraInicio(rackReservaDTO.getHoraInicio());
        rackExistente.setNombreReservante(rackReservaDTO.getNombreReservante());
        rackExistente.setIdCliente(rackReservaDTO.getIdCliente());
        rackExistente.setIdReserva(idReserva);

        return rackReservaRepository.save(rackExistente);
    }

    public Boolean deleteRackReservaById(Long idReserva) {
        RackReserva rackReserva = rackReservaRepository.findRackReservaByIdReserva(idReserva);
        if (rackReserva == null) throw new EntityNotFoundException("Rack Reserva no encontrado");

        rackReservaRepository.delete(rackReserva);
        return true;
    }
}
