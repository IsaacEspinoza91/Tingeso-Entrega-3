package com.kartingrm.reservas_comprobantes_service.service;

import com.kartingrm.reservas_comprobantes_service.entity.ClienteReserva;
import com.kartingrm.reservas_comprobantes_service.entity.ClienteReservaId;
import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.ClienteDTO;
import com.kartingrm.reservas_comprobantes_service.model.ClienteReservaRequest;
import com.kartingrm.reservas_comprobantes_service.modelbase.BaseService;
import com.kartingrm.reservas_comprobantes_service.repository.ClienteReservaRepository;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteReservaService extends BaseService {

    private final ClienteReservaRepository clienteReservaRepository;

    public ClienteReservaService(ClienteReservaRepository clienteReservaRepository, RestTemplate restTemplate) {
        super(restTemplate);
        this.clienteReservaRepository = clienteReservaRepository;
    }


    public List<ClienteReserva> obtenerIntegrantesByIdReserva(Long idReserva) {
        return clienteReservaRepository.findByIdReserva(idReserva);
    }

    public List<ClienteDTO> obtenerIntegrantesDTOByIdReserva(Long idReserva) {
        List<ClienteReserva> idsClientesDeReserva = clienteReservaRepository.findByIdReserva(idReserva);
        List<ClienteDTO> clientes = new ArrayList<>();

        for(ClienteReserva idCliente : idsClientesDeReserva) {
            ClienteDTO cliente = restTemplate.getForObject(CLIENTE_ENDPOINT + idCliente.getIdCliente(), ClienteDTO.class);
            clientes.add(cliente);
        }

        return clientes;
    }

    public boolean agregarIntegrante(Long idCliente, Long idReserva) {
        Reserva reserva = obtenerReserva(idReserva);
        // Verificar si ya existe la relación cliente reserva
        if (clienteReservaRepository.existsByIdClienteAndIdReserva(idCliente, idReserva)) {
            throw new IllegalStateException("El cliente ya esta en esta reserva");
        }

        int cantidadIntegrantes = reserva.getTotalPersonas();

        // Verificar límite de integrantes
        int integrantesActuales = clienteReservaRepository.countByIdReserva(idReserva);
        if (integrantesActuales >= cantidadIntegrantes) {
            throw new IllegalStateException("La reserva ha alcanzado el máximo de integrantes");
        }

        // Crear y guardar la relación
        ClienteReserva nuevaRelacion = new ClienteReserva(idCliente, idReserva);
        clienteReservaRepository.save(nuevaRelacion);

        // Se guarda la relación en la tabla Cliente_reserva de MC3
        ClienteReservaRequest relacionRequest = new ClienteReservaRequest(
                new ClienteReservaId(idCliente,idReserva),
                reserva.getFecha(),
                reserva.getEstado());
        HttpEntity<ClienteReservaRequest> requestBodyClienteReserva = new HttpEntity<>(relacionRequest);

        restTemplate.postForObject(
                CLIENTE_RESERVA_ENDPOINT,
                requestBodyClienteReserva,
                ClienteReservaRequest.class);

        return true;
    }

    public void quitarIntegrante(Long idCliente, Long idReserva) {
        ClienteReservaId id = new ClienteReservaId(idCliente, idReserva);
        if (!clienteReservaRepository.existsById(id)) throw new IllegalArgumentException("El cliente no esta relacionado a esta reserva");

        // Eliminamos la relación de la tabla Cliente_reserva de MS3
        restTemplate.delete(CLIENTE_RESERVA_ENDPOINT + "reserva/{idReserva}/cliente/{idCliente}", idReserva, idCliente);

        clienteReservaRepository.deleteById(id);
    }

}