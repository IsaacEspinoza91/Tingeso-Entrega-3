package com.kartingrm.reservas_comprobantes_service.modelbase;

import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.ClienteDTO;
import com.kartingrm.reservas_comprobantes_service.model.PlanDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

public abstract class BaseService {

    public static final String URL_CLIENTE_DESC_FRECU_MS = "http://cliente-desc-frecu-service";
    public static final String CLIENTE_DESC_FRECU_BASE = "/api/cliente-service";
    public static final String CLIENTE_ENDPOINT = URL_CLIENTE_DESC_FRECU_MS + CLIENTE_DESC_FRECU_BASE + "/cliente/";
    public static final String CLIENTE_RESERVA_ENDPOINT = URL_CLIENTE_DESC_FRECU_MS + CLIENTE_DESC_FRECU_BASE + "/cliente-reserva/";

    public static final String URL_PLAN_MS = "http://plan-service";
    public static final String PLAN_BASE = "/api/plan";
    public static final String PLAN_ENDPOINT = URL_PLAN_MS + PLAN_BASE + "/planes/";

    public static final String URL_DIAS_ESPECIALES_MS = "http://dias-especiales-service";
    public static final String DIAS_ESPECIALES_BASE = "/api/dias-especiales-service";
    public static final String DIAS_FERIADOS_ENDPOINT = URL_DIAS_ESPECIALES_MS + DIAS_ESPECIALES_BASE + "/dias-feriados/";
    public static final String CLIENTE_CUMPLEANIOS_ENDPOINT = URL_DIAS_ESPECIALES_MS + DIAS_ESPECIALES_BASE + "/cliente-cumpleanios/";

    public static final String URL_RACK_SEMANAL_MS = "http://rack-semanal-service";
    public static final String RACK_SEMANAL_BASE = "/api/rack-semanal-service";
    public static final String RACK_RESERVA_ENDPOINT = URL_RACK_SEMANAL_MS + RACK_SEMANAL_BASE + "/rack-reserva/";

    public static final String URL_DESCUENTO_GRUPO_MS = "http://descuento-grupo-service";
    public static final String DESCUENTO_GRUPO_BASE = "/api/descuento-grupo-service";
    public static final String DESCUENTO_GRUPO_ENDPOINT = URL_DESCUENTO_GRUPO_MS + DESCUENTO_GRUPO_BASE + "/desc-grupo/";

    public static final String URL_REPORTES_MS = "http://reportes-service";
    public static final String REPORTES_BASE = "/api/reportes-service";
    public static final String REPORTES_RANGO_ENDPOINT = URL_REPORTES_MS + REPORTES_BASE + "/segun-rango-personas/";
    public static final String REPORTES_PLAN_ENDPOINT = URL_REPORTES_MS + REPORTES_BASE + "/segun-plan/";

    public static final String URL_RESERVA_MS = "http://reservas-comprobantes-service";
    public static final String RESERVA_BASE = "/api/reservas-comprobantes-service";
    public static final String RESERVAS_ENDPOINT = URL_RESERVA_MS + RESERVA_BASE + "/reservas/";


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

    protected Reserva obtenerReserva(Long idReserva) {
        try {
            Reserva reserva = restTemplate.getForObject(RESERVAS_ENDPOINT + idReserva, Reserva.class);
            if (reserva == null) throw new EntityNotFoundException("No se encontró reserva con id: " + idReserva);

            return reserva;
        } catch (ResourceAccessException ex) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Servicio de clientes no disponible: " + ex.getMessage());
        }
    }

    protected List<Integer> obtenerIdsClientes(String nombre) {
        try{
            List<Integer> ids = restTemplate.getForObject(CLIENTE_ENDPOINT +"ids-busqueda-nombre/" + nombre, List.class);
            if (ids == null) throw new EntityNotFoundException("Error al buscar clientes.");

            return ids;
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
