package com.kartingrm.reservas_comprobantes_service.service;

import com.kartingrm.reservas_comprobantes_service.entity.ClienteReserva;
import com.kartingrm.reservas_comprobantes_service.entity.ClienteReservaId;
import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.ClienteDTO;
import com.kartingrm.reservas_comprobantes_service.model.ClienteReservaRequest;
import com.kartingrm.reservas_comprobantes_service.repository.ClienteReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteReservaService {

    @Autowired
    private ClienteReservaRepository clienteReservaRepository;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private RestTemplate restTemplate;


    public List<ClienteReserva> obtenerIntegrantesByIdReserva(Long idReserva) {
        return clienteReservaRepository.findByIdReserva(idReserva);
    }

    public List<ClienteDTO> obtenerIntegrantesDTOByIdReserva(Long idReserva) {
        List<ClienteReserva> idsClientesDeReserva = clienteReservaRepository.findByIdReserva(idReserva);
        List<ClienteDTO> clientes = new ArrayList<>();

        for(ClienteReserva idCliente : idsClientesDeReserva) {

            // Obtener Cliente
            ClienteDTO cliente = restTemplate.getForObject("http://CLIENTEDESCFRECU/api/cliente-service/cliente/"
                    + idCliente.getIdCliente(), ClienteDTO.class);

            clientes.add(cliente);
        }
        return clientes;
    }

    public boolean agregarIntegrante(Long idCliente, Long idReserva) {
        // Falta verificar existencia de cliente

        // Verificar si existe reserva. Si no existe genera una excepcion
        Reserva reserva = reservaService.getReservaById(idReserva);

        // Verificar si ya existe la relación cliente reserva
        if (clienteReservaRepository.existsByIdClienteAndIdReserva(idCliente, idReserva)) {
            throw new IllegalStateException("El cliente ya esta en esta reserva");
        }

        // Obtener cantidad de integrantes para la reserva
        int cantidadIntegrantes = reserva.getTotalPersonas();

        // Verificar límite de integrantes
        int integrantesActuales = clienteReservaRepository.countByIdReserva(idReserva);
        if (integrantesActuales >= cantidadIntegrantes) {
            throw new IllegalStateException("La reserva ha alcanzado el máximo de integrantes");
        }

        // Crear y guardar la relación
        ClienteReserva nuevaRelacion = new ClienteReserva(idCliente, idReserva);
        clienteReservaRepository.save(nuevaRelacion);

        // Se guarda la relacion en la tabla Cliente_reserva de MC3
        ClienteReservaRequest relacionRequest = new ClienteReservaRequest(
                new ClienteReservaId(idCliente,idReserva),
                reserva.getFecha(),
                reserva.getEstado());
        HttpEntity<ClienteReservaRequest> requestBodyClienteReserva = new HttpEntity<ClienteReservaRequest>(relacionRequest);

        ClienteReservaRequest clienteReservaRequestObtenida = restTemplate.postForObject(
                "http://CLIENTEDESCFRECU/api/cliente-service/cliente-reserva/",
                requestBodyClienteReserva,
                ClienteReservaRequest.class);

        return true;
    }

    public void quitarIntegrante(Long idCliente, Long idReserva) {
        ClienteReservaId id = new ClienteReservaId(idCliente, idReserva);
        if (!clienteReservaRepository.existsById(id)) {
            throw new IllegalArgumentException("El cliente no esta relacionado a esta reserva");
        }

        // Eliminamos la relacion de la tabla Cliente_reserva de MC3   reserva/1/cliente/2
        restTemplate.delete("http://CLIENTEDESCFRECU/api/cliente-service/cliente-reserva/reserva/{idReserva}/cliente/{idCliente}", idReserva, idCliente);


        clienteReservaRepository.deleteById(id);
    }


}