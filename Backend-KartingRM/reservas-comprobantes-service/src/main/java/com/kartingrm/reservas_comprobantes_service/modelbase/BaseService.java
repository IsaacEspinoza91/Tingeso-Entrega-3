package com.kartingrm.reservas_comprobantes_service.modelbase;

import com.kartingrm.reservas_comprobantes_service.model.ClienteDTO;
import com.kartingrm.reservas_comprobantes_service.model.PlanDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

public abstract class BaseService {

    static final String URL_CLIENTE_DESC_FRECU_MS = "http://cliente-desc-frecu-service";
    static final String CLIENTE_DESC_FRECU_BASE = "/api/cliente-service";
    static final String CLIENTE_ENDPOINT = URL_CLIENTE_DESC_FRECU_MS + CLIENTE_DESC_FRECU_BASE + "/cliente/";

    static final String URL_PLAN_MS = "http://plan-service";
    static final String PLAN_BASE = "/api/plan-service";
    static final String PLAN_ENDPOINT = URL_PLAN_MS + PLAN_BASE + "/planes/";

    static final String URL_DIAS_ESPECIALES_MS = "http://dias-especiales-service";
    static final String DIAS_ESPECIALES_BASE = "/api/dias-especiales-service";
    static final String DIAS_FERIADOS_ENDPOINT = URL_DIAS_ESPECIALES_MS + DIAS_ESPECIALES_BASE + "/dias-feriados/";

    protected final RestTemplate restTemplate;
    protected BaseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    // Obtener plan mediante petición http
    protected PlanDTO obtenerPlan(Long idPlan) {
        try {
            PlanDTO plan = restTemplate.getForObject(PLAN_ENDPOINT + idPlan, PlanDTO.class);
            if (plan == null) throw new EntityNotFoundException("No se encontró el plan con id: " + idPlan);

            return plan;
        } catch (ResourceAccessException ex) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Servicio de planes no disponible: " + ex.getMessage());
        }
    }

    // Obtener cliente mediante petición http
    protected ClienteDTO obtenerCliente(Long idCliente) {
        try{
            ClienteDTO cliente = restTemplate.getForObject(CLIENTE_ENDPOINT + idCliente, ClienteDTO.class);
            if (cliente == null) throw new EntityNotFoundException("No se encontró cliente con id: " + idCliente);

            return cliente;
        } catch (ResourceAccessException ex) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Servicio de clientes no disponible: " + ex.getMessage());
        }
    }

    protected boolean esDiaFeriado(LocalDate fecha) {
        try {
            Boolean esFeriado = restTemplate.getForObject(DIAS_FERIADOS_ENDPOINT + "esFeriado?fecha=" + fecha, Boolean.class);
            if (esFeriado == null) throw new IllegalStateException("El servicio de dias especiales no devolvió respuesta válida");

            return esFeriado;
        } catch (ResourceAccessException ex) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Servicio de dias especiales no disponible: " + ex.getMessage());
        }
    }

}
