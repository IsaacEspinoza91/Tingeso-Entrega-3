package com.kartingrm.rack_semanal_service.service;

import com.kartingrm.rack_semanal_service.entity.RackReserva;
import com.kartingrm.rack_semanal_service.repository.RackReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class RackReservaService {

    @Autowired
    private RackReservaRepository rackReservaRepository;

    public List<RackReserva> getRackReservas() {
        return rackReservaRepository.findAll();
    }

    public RackReserva getRackReservaById(Long id) {
        Optional<RackReserva> rackReserva = rackReservaRepository.findById(id);
        if (rackReserva.isEmpty()) throw new EntityNotFoundException("Rack Reserva no encontrado");

        return rackReserva.get();
    }

    public RackReserva createRackReserva(RackReserva rackReserva) {
        return rackReservaRepository.save(rackReserva);
    }

    public RackReserva updateRackReserva(Long id, RackReserva rackReserva) {
        Optional<RackReserva> rackReservaOptional = rackReservaRepository.findById(id);
        if (rackReservaOptional.isEmpty()) throw new EntityNotFoundException("Rack Reserva no encontrado");

        rackReserva.setIdReserva(id);
        return rackReservaRepository.save(rackReserva);
    }

    public Boolean deleteRackReservaById(Long id) {
        Optional<RackReserva> rackReservaOptional = rackReservaRepository.findById(id);
        if (rackReservaOptional.isEmpty()) throw new EntityNotFoundException("Rack Reserva no encontrado");

        rackReservaRepository.deleteById(id);
        return true;
    }
}
