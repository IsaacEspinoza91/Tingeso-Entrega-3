package com.kartingrm.dias_especiales_service.service;

import com.kartingrm.dias_especiales_service.entity.ClienteCumpleanios;
import com.kartingrm.dias_especiales_service.repository.ClienteCumpleaniosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteCumpleaniosService {

    @Autowired
    private ClienteCumpleaniosRepository clienteCumpleaniosRepository;


    public List<ClienteCumpleanios> getClientesCumpleanios() {
        return clienteCumpleaniosRepository.findAll();
    }

    public ClienteCumpleanios getClienteCumpleaniosByIdCliente(Long id) {
        Optional<ClienteCumpleanios> clienteCumpleanios = clienteCumpleaniosRepository.findById(id);
        if (clienteCumpleanios.isEmpty()) throw new EntityNotFoundException("Registro clienteCumpleanios no encontrado");

        return clienteCumpleanios.get();
    }

    public ClienteCumpleanios createClienteCumpleanios(ClienteCumpleanios clienteCumpleanios) {
        return clienteCumpleaniosRepository.save(clienteCumpleanios);
    }

    public ClienteCumpleanios updateClienteCumpleaniosByIdCliente(Long idCliente, ClienteCumpleanios clienteCumpleanios) {
        Optional<ClienteCumpleanios> clienteCumpleaniosActualizado = clienteCumpleaniosRepository.findById(idCliente);
        if (clienteCumpleaniosActualizado.isEmpty()) throw new EntityNotFoundException("Registro clienteCumpleanios no encontrado");

        clienteCumpleanios.setIdCliente(idCliente);
        return clienteCumpleaniosRepository.save(clienteCumpleanios);
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
