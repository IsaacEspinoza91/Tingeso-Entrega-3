package com.kartingrm.cliente_desc_frecu_service.service;

import com.kartingrm.cliente_desc_frecu_service.DTO.ClienteCumpleaniosRequest;
import com.kartingrm.cliente_desc_frecu_service.entity.Cliente;
import com.kartingrm.cliente_desc_frecu_service.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestTemplate restTemplate;


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

    public Cliente createCliente(Cliente cliente) {
        Cliente clienteCreado =  clienteRepository.save(cliente);

        // Se guarda relacion cliente cumpleanios en la tabla correspondiente en MC4
        ClienteCumpleaniosRequest cumpleaniosRequest = new ClienteCumpleaniosRequest(clienteCreado.getId(), clienteCreado.getFechaNacimiento());
        HttpEntity<ClienteCumpleaniosRequest> cumpleaniosRequestBody = new HttpEntity<>(cumpleaniosRequest);

        ClienteCumpleaniosRequest respuesta = restTemplate.postForObject(
                "http://dias-especiales-service/api/dias-especiales-service/cliente-cumpleanios/",
                cumpleaniosRequestBody,
                ClienteCumpleaniosRequest.class);

        return clienteCreado;
    }

    public Cliente updateCliente(Long id, Cliente cliente) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isEmpty()) throw new EntityNotFoundException("Cliente id " + id + " no encontrado");

        cliente.setId(id);

        // Se actualiza la el cumpleanios en la tabla cliente_cumpleanios MC4
        // Solo en caso de actualizacion de cumpleanios se hace la peticion http a MC4
        if (cliente.getFechaNacimiento() != clienteOptional.get().getFechaNacimiento()) {
            HttpEntity<ClienteCumpleaniosRequest> cumpleaniosRequestPutBody = new HttpEntity<>(
                    new ClienteCumpleaniosRequest(null, cliente.getFechaNacimiento())
            );
            restTemplate.put(
                    "http://dias-especiales-service/api/dias-especiales-service/cliente-cumpleanios/" + id,
                    cumpleaniosRequestPutBody,
                    ClienteCumpleaniosRequest.class);
        }

        Cliente clienteActualizado = clienteRepository.save(cliente);
        return clienteActualizado;
    }

    public Boolean deleteCliente(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isEmpty()) throw new EntityNotFoundException("Cliente id " + id + " no encontrado");

        clienteRepository.deleteById(id);

        // Eliminar relacion de tabla cliente_cumpleanios MC4
        restTemplate.delete("http://dias-especiales-service/api/dias-especiales-service/cliente-cumpleanios/" + id);
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

        String nombreCompleto = clienteOptional.get().getNombre() + " " + clienteOptional.get().getApellido();
        return nombreCompleto;
    }
}
