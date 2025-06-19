package com.kartingrm.diasespecialesservice.service;

import com.kartingrm.diasespecialesservice.dto.ClienteCumpleaniosDTO;
import com.kartingrm.diasespecialesservice.entity.ClienteCumpleanios;
import com.kartingrm.diasespecialesservice.repository.ClienteCumpleaniosRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteCumpleaniosService {

    private ClienteCumpleaniosRepository clienteCumpleaniosRepository;
    public ClienteCumpleaniosService(ClienteCumpleaniosRepository clienteCumpleaniosRepository) {
        this.clienteCumpleaniosRepository = clienteCumpleaniosRepository;
    }

    public List<ClienteCumpleanios> getClientesCumpleanios() {
        return clienteCumpleaniosRepository.findAll();
    }

    public ClienteCumpleanios getClienteCumpleaniosByIdCliente(Long id) {
        Optional<ClienteCumpleanios> clienteCumpleanios = clienteCumpleaniosRepository.findById(id);
        if (clienteCumpleanios.isEmpty()) throw new EntityNotFoundException("Registro clienteCumpleanios no encontrado");

        return clienteCumpleanios.get();
    }

    public ClienteCumpleanios createClienteCumpleanios(ClienteCumpleaniosDTO clienteCumpleaniosDTO) {
        if (clienteCumpleaniosDTO == null) throw new EntityNotFoundException("ClienteCumpleanios no puede ser nulo");

        ClienteCumpleanios clienteCumpleanios = new ClienteCumpleanios();
        clienteCumpleanios.setIdCliente(clienteCumpleaniosDTO.getIdCliente());
        clienteCumpleanios.setFecha(clienteCumpleaniosDTO.getFecha());

        return clienteCumpleaniosRepository.save(clienteCumpleanios);
    }

    public ClienteCumpleanios updateClienteCumpleaniosByIdCliente(Long idCliente, ClienteCumpleaniosDTO clienteCumpleanios) {
        ClienteCumpleanios clienteCumpleaniosExistente = clienteCumpleaniosRepository.findById(idCliente).orElseThrow(() -> new  EntityNotFoundException("ClienteCumpleanios no encontrado"));

        clienteCumpleaniosExistente.setIdCliente(clienteCumpleanios.getIdCliente());
        clienteCumpleaniosExistente.setFecha(clienteCumpleanios.getFecha());

        return clienteCumpleaniosRepository.save(clienteCumpleaniosExistente);
    }

    public Boolean deleteClienteCumpleaniosByIdCliente(Long idCliente) {
        Optional<ClienteCumpleanios> clienteCumpleaniosActualizado = clienteCumpleaniosRepository.findById(idCliente);
        if (clienteCumpleaniosActualizado.isEmpty()) throw new EntityNotFoundException("Registro clienteCumpleanios no encontrado");

        clienteCumpleaniosRepository.deleteById(idCliente);
        return true;
    }

    // Obtiene valor booleana sobre si el cliente cumple anios en determinada fecha
    public boolean estaDeCumpleanios(Long idCliente, LocalDate fecha) {
        return clienteCumpleaniosRepository.estaDeCumpleanios(idCliente, fecha);
    }

}
