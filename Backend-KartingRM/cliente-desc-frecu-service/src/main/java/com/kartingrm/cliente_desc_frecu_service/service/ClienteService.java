package com.kartingrm.cliente_desc_frecu_service.service;

import com.kartingrm.cliente_desc_frecu_service.dto.ClienteCumpleaniosRequest;
import com.kartingrm.cliente_desc_frecu_service.dto.ClienteDTO;
import com.kartingrm.cliente_desc_frecu_service.entity.Cliente;
import com.kartingrm.cliente_desc_frecu_service.repository.ClienteRepository;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    // Constantes
    static final String URL_DIAS_ESPECIALES_MS = "http://dias-especiales-service";
    static final String DIAS_ESPECIALES_BASE = "/api/dias-especiales-service";
    static final String CLIENTE_CUMPLEANIOS_ENDPOINT = DIAS_ESPECIALES_BASE + "/cliente-cumpleanios/";

    private ClienteRepository clienteRepository;
    private RestTemplate restTemplate;
    public ClienteService(ClienteRepository clienteRepository, RestTemplate restTemplate) {
        this.clienteRepository = clienteRepository;
        this.restTemplate = restTemplate;
    }


    public List<Cliente> getClientes() {
        return clienteRepository.findAll();
    }

    public List<Cliente> getClientesActivos() {
        return clienteRepository.findClientesByActivo(true);
    }

    public List<Cliente> getClientesInactivos() {
        return clienteRepository.findClientesByActivo(false);
    }

    public Cliente getClienteById(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isEmpty()) throw new EntityNotFoundException("Cliente id " + id + " no encontrado");

        return cliente.get();
    }

    public Cliente getClienteByRut(String rut) {
        Optional<Cliente> cliente = clienteRepository.findClienteByRut(rut);
        if (cliente.isEmpty()) throw new EntityNotFoundException("Cliente rut " + rut + " no encontrado");

        return cliente.get();
    }

    public List<Cliente> getClientesByNombreApellido(String nombre, String apellido) {
        Optional<List<Cliente>> cliente = clienteRepository.findClienteByNombreAndApellido(nombre, apellido);
        if (cliente.isEmpty()) throw new EntityNotFoundException("Cliente no encontrado");

        return cliente.get();
    }

    public Cliente createCliente(ClienteDTO clienteDTO) {
        if (clienteDTO == null) throw new EntityNotFoundException("Cliente nulo");

        Cliente clienteCreado = new Cliente();
        clienteCreado.setNombre(clienteDTO.getNombre());
        clienteCreado.setApellido(clienteDTO.getApellido());
        clienteCreado.setRut(clienteDTO.getRut());
        clienteCreado.setCorreo(clienteDTO.getCorreo());
        clienteCreado.setTelefono(clienteDTO.getTelefono());
        clienteCreado.setActivo(clienteDTO.isActivo());
        clienteCreado.setFechaNacimiento(clienteDTO.getFechaNacimiento());

        clienteRepository.save(clienteCreado);

        // Se guarda relacion cliente cumpleanios en la tabla correspondiente en MS44
        ClienteCumpleaniosRequest cumpleaniosRequest = new ClienteCumpleaniosRequest(clienteCreado.getId(), clienteCreado.getFechaNacimiento());
        HttpEntity<ClienteCumpleaniosRequest> cumpleaniosRequestBody = new HttpEntity<>(cumpleaniosRequest);
        restTemplate.postForObject(URL_DIAS_ESPECIALES_MS + CLIENTE_CUMPLEANIOS_ENDPOINT, cumpleaniosRequestBody, ClienteCumpleaniosRequest.class);

        return clienteCreado;
    }

    public Cliente updateCliente(Long id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente id " + id + " no encontrado"));

        LocalDate fechaNacimientoAnterior = clienteExistente.getFechaNacimiento();

        // Actualizar campos
        clienteExistente.setNombre(clienteDTO.getNombre());
        clienteExistente.setApellido(clienteDTO.getApellido());
        clienteExistente.setRut(clienteDTO.getRut());
        clienteExistente.setCorreo(clienteDTO.getCorreo());
        clienteExistente.setTelefono(clienteDTO.getTelefono());
        clienteExistente.setActivo(clienteDTO.isActivo());
        clienteExistente.setFechaNacimiento(clienteDTO.getFechaNacimiento());

        // actualizacion de cumpleanios en tabla cliente_cumpleanios MS4
        if (!clienteExistente.getFechaNacimiento().equals(fechaNacimientoAnterior)) {
            HttpEntity<ClienteCumpleaniosRequest> cumpleaniosRequestPutBody = new HttpEntity<>(
                    new ClienteCumpleaniosRequest(clienteExistente.getId(), clienteExistente.getFechaNacimiento())
            );
            restTemplate.put(URL_DIAS_ESPECIALES_MS + CLIENTE_CUMPLEANIOS_ENDPOINT + id, cumpleaniosRequestPutBody, ClienteCumpleaniosRequest.class);
        }

        return clienteRepository.save(clienteExistente);
    }

    public Boolean deleteCliente(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isEmpty()) throw new EntityNotFoundException("Cliente id " + id + " no encontrado");

        clienteRepository.deleteById(id);

        // Eliminar relacion de tabla cliente_cumpleanios MS4
        restTemplate.delete(URL_DIAS_ESPECIALES_MS + CLIENTE_CUMPLEANIOS_ENDPOINT + id);
        return true;
    }

    public Cliente inactivateCliente(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isEmpty()) {
            throw new EntityNotFoundException("Cliente " + id + " no encontrado");
        } else {
            clienteOptional.get().setActivo(false);
            return clienteRepository.save(clienteOptional.get());
        }
    }


    public Cliente activateCliente(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isEmpty()) {
            throw new EntityNotFoundException("Cliente " + id + " no encontrado");
        } else {
            clienteOptional.get().setActivo(true);
            return clienteRepository.save(clienteOptional.get());
        }
    }

    public String getNombreCompletoClienteByid(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isEmpty()) throw new EntityNotFoundException("Cliente id " + id + " no encontrado");

        return clienteOptional.get().getNombre() + " " + clienteOptional.get().getApellido();
    }
}
