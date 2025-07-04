package com.kartingrm.reservas_comprobantes_service.service;

import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.*;
import com.kartingrm.reservas_comprobantes_service.modelbase.BaseService;
import com.kartingrm.reservas_comprobantes_service.repository.ReservaRepository;
import com.kartingrm.reservas_comprobantes_service.utils.ReservaCreationException;
import com.kartingrm.reservas_comprobantes_service.utils.ServiceIntegrationException;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService extends BaseService {

    // Constantes
    private static final String ESTADO_COMPLETADA = "completada";
    private static final String ESTADO_CONFIRMADA = "confirmada";


    private final ReservaRepository reservaRepository;
    private final ClienteReservaService clienteReservaService;
    private final ComprobanteService comprobanteService;
    public ReservaService(ReservaRepository reservaRepository, ClienteReservaService clienteReservaService, ComprobanteService comprobanteService, RestTemplate restTemplate) {
        super(restTemplate);
        this.reservaRepository = reservaRepository;
        this.clienteReservaService = clienteReservaService;
        this.comprobanteService = comprobanteService;
    }


    public List<Reserva> getReservas() {
        return reservaRepository.findAll();
    }

    public List<ReservaDTO> getReservasDTO() {
        List<Reserva> reservas = reservaRepository.findAll();
        return createReservasConFormatoDTO(reservas);
    }

    public Reserva getReservaById(Long id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        if (reserva.isEmpty()) throw new EntityNotFoundException("Reserva de ID " + id + " no encontrada");

        return reserva.get();
    }

    public ReservaDTO getReservaDTOById(Long id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        if (reserva.isEmpty()) throw new EntityNotFoundException("Reserva de ID " + id + " no encontrada");

        ClienteDTO cliente = obtenerCliente(reserva.get().getIdReservante());
        PlanDTO plan = obtenerPlan(reserva.get().getIdPlan());

        return new ReservaDTO(reserva.get(), plan, cliente);
    }

    // Obtener todas las reservas de un cliente según id
    public List<Reserva> getReservasByIdReservante(Long idCliente) {
        return reservaRepository.findReservasByIdReservante(idCliente);
    }


    // Obtener todas las reservas DTO de un cliente según id
    public List<ReservaDTO> getReservasDTOByIdReservante(Long idCliente) {
        List<Reserva> reservas = reservaRepository.findReservasByIdReservante(idCliente);
        return createReservasConFormatoDTO(reservas);
    }

    // Obtener todas las reservas encontradas según nombre parcial de cliente
    public List<ReservaDTO> getReservasDTOByNombreParcialCliente(String nombre) {
        List<ReservaDTO> reservas = new ArrayList<>();
        List<Integer> listaIdsClientes = obtenerIdsClientes(nombre);

        for(Integer idCliente : listaIdsClientes){
            reservas.addAll(getReservasDTOByIdReservante(idCliente.longValue()));
        }
        return reservas;
    }


    // Crear reserva
    // Considera que no existan reservas previas en el horario, además ingresa automáticamente la hora de fin según el plan
    // El cuerpo ya tiene id cliente reservante e id plan
    public Reserva createReserva(ReservaRequest reservaRequest) {
        Reserva reserva = new Reserva(reservaRequest);
        ClienteDTO cliente = obtenerCliente(reserva.getIdReservante());
        PlanDTO plan = obtenerPlan(reserva.getIdPlan());

        if (reserva.getHoraInicio() != null) {
            LocalTime horaFinalCalculada = determinarHoraFin(reserva, plan);
            reserva.setHoraFin(horaFinalCalculada);

            // Verificar que no existan reservas entre ambas horas
            if (existeReservaEntreDosHoras(reserva.getFecha(), reserva.getHoraInicio(), horaFinalCalculada)) throw new IllegalStateException("Ya existe una reserva con ese horario");

            // Verificar horario válido de atención
            Boolean esFinDeSemana = reserva.getFecha().getDayOfWeek().getValue() >= 6;
            Boolean esFeriado = esDiaFeriado(reserva.getFecha());
            esHorarioValido(esFeriado, esFinDeSemana, reserva);

            reservaRepository.save(reserva);

            guardarReservaEnRack(reserva, cliente);

            return reserva;
        } else {
            throw new EntityNotFoundException("Cliente no encontrado, plan no existe, o hora errónea");
        }
    }


    // Función que crea una reserva completamente, agregando integrantes y creando el comprobante.
    // Mejor usabilidad para el cliente
    public void createReservaCompleta(ReservaCreateRequest req) {
        ReservaRequest reservaRequest = new ReservaRequest(req.getFecha(), req.getHoraInicio(), req.getEstado(), req.getTotalPersonas(), req.getIdPlan(), req.getIdReservante());
        Reserva reserva = null;

        try {
            reserva = createReserva(reservaRequest);

            for (Long idIntegrante : req.getIdsIntegrantes()) {
                clienteReservaService.agregarIntegrante(idIntegrante, reserva.getId());
            }

            comprobanteService.createComprobante(reserva.getId(), req.getDescuentoExtra());

        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new ReservaCreationException("Error interno al procesar la reserva", e);
        }
    }


    // Update de valor de reserva. Permite cambio de plan y de cliente reservante
    public Reserva updateReserva(Long id, ReservaRequest reservaRequest) {
        Reserva reserva = new Reserva(reservaRequest);

        Optional<Reserva> reservaOriginalOptional = reservaRepository.findById(id);
        if (reservaOriginalOptional.isEmpty()) throw new EntityNotFoundException("Reserva id " + id + " no encontrado");

        String estadoAnterior = reservaOriginalOptional.get().getEstado();
        // Reserva actualizada conserva id de la reserva original
        reserva.setId(id);

        PlanDTO plan = obtenerPlan(reserva.getIdPlan());
        ClienteDTO cliente = obtenerCliente(reserva.getIdReservante());

        // Solo permite actualizar la hora inicial, en consecuencia se actualiza la hora final
        LocalTime horaFinalCalculada = determinarHoraFin(reserva, plan);
        reserva.setHoraFin(horaFinalCalculada);

        // Verificar horario válido de atención
        Boolean esFinDeSemana = reserva.getFecha().getDayOfWeek().getValue() >= 6;// Determina fin de semana o no
        Boolean esFeriado = restTemplate.getForObject(DIAS_FERIADOS_ENDPOINT + "esFeriado?fecha=" + reserva.getFecha(),Boolean.class);
        esHorarioValido(esFeriado, esFinDeSemana, reserva);

        // Se guarda reserva en base de datos
        reservaRepository.save(reserva);

        // Guardar relación de reserva en la tabla rack_reserva, para obtener el rack semanal desde el MC6
        // Estado nuevo es completada o confirmada
        if (reserva.getEstado().equals(ESTADO_COMPLETADA) || reserva.getEstado().equals(ESTADO_CONFIRMADA)) {
            // Estado anterior era cancelada
            if (!estadoAnterior.equals(ESTADO_COMPLETADA) && !estadoAnterior.equals(ESTADO_CONFIRMADA)) {

                RackReservaRequest rackReservaRequest = new RackReservaRequest(
                        reserva.getId(),
                        cliente.getId(),
                        cliente.getNombre() + " " + cliente.getApellido(),
                        reserva.getFecha(),
                        reserva.getHoraInicio(),
                        reserva.getHoraFin()
                );

                HttpEntity<RackReservaRequest> rackReservaBody = new HttpEntity<>(rackReservaRequest);

                restTemplate.postForObject(RACK_RESERVA_ENDPOINT, rackReservaBody, RackReservaRequest.class);

            }
            // Else, Caso mantiene estado era completada o confirmada. No realiza petición http
        } else {// Estado nuevo es cancelada
            // Caso estado anterior era completada o confirmada
            if (estadoAnterior.equals(ESTADO_COMPLETADA) || estadoAnterior.equals(ESTADO_CONFIRMADA)) {
                restTemplate.delete(RACK_RESERVA_ENDPOINT + reserva.getId());
            }
            // Else, Caso mantiene estado cancelado. No hace petición http
        }

        return reserva;
    }



    public boolean deleteReserva(Long id) {
        Optional<Reserva> reservaOriginalOptional = reservaRepository.findById(id);
        if (reservaOriginalOptional.isEmpty()) throw new EntityNotFoundException("Reserva id " + id + " no encontrado");

        reservaRepository.deleteById(id);
        restTemplate.delete(RACK_RESERVA_ENDPOINT + id);
        return true;
    }



    //  Métodos privados. Lógica interna

    // Crea el formato de ReservasDTO según reservas (Evita código duplicado)
    private List<ReservaDTO> createReservasConFormatoDTO(List<Reserva> reservas) {
        List<ReservaDTO> reservasDTO = new ArrayList<>();
        for (Reserva reservaActual : reservas) {
            ClienteDTO cliente = obtenerCliente(reservaActual.getIdReservante());
            PlanDTO plan = obtenerPlan(reservaActual.getIdPlan());

            ReservaDTO reservaDTO = new ReservaDTO(reservaActual, plan, cliente);

            reservasDTO.add(reservaDTO);
        }
        return reservasDTO;
    }

    // Obtener reservas existentes entre dos horas de un día. Condición para crear reservas
    private boolean existeReservaEntreDosHoras(LocalDate fecha, LocalTime horaInicio, LocalTime horaFinal) {
        return reservaRepository.existeReservaEntreDosHoras(fecha, horaInicio, horaFinal);
    }

    // Validación si reserva es en horario de trabajo
    //  lunes a viernes: 14:00 a 22:00
    //  sábados, domingos, feriados: 10:00 a 22:00
    public void esHorarioValido(Boolean diaFeriado, Boolean diaFinDeSemana, Reserva reserva) {
        // Horario fuera semana
        if (diaFeriado || diaFinDeSemana){
            if (reserva.getHoraInicio().isBefore(LocalTime.of(10,0,0)) ||
                    reserva.getHoraFin().isAfter(LocalTime.of(22,0,0))) {
                throw new IllegalStateException("Horario incorrecto. Domingos, sábados y feriados: 10:00 a 22:00");
            }
        } else { // Horario semana
            if (reserva.getHoraInicio().isBefore(LocalTime.of(14,0,0)) ||
                    reserva.getHoraFin().isAfter(LocalTime.of(22,0,0))) {
                throw new IllegalStateException("Horario incorrecto. Lunes a Viernes: 14:00 a 22:00");
            }
        }
    }

    // Determinar hora de fin sumando minutos del plan a la hora inicial
    public LocalTime determinarHoraFin(Reserva reserva, PlanDTO plan) {
        return reserva.getHoraInicio().plusMinutes(plan.getDuracionTotal());
    }


    // Guardar relación de reserva en la tabla rack_reserva, para obtener el rack semanal desde el MC6
    // Solo en caso de reserva confirmada o completada
    private void guardarReservaEnRack(Reserva reserva, ClienteDTO cliente) {
        if (reserva.getEstado().equals(ESTADO_COMPLETADA) || reserva.getEstado().equals(ESTADO_CONFIRMADA)) {
            RackReservaRequest rackReservaRequest = new RackReservaRequest(
                    reserva.getId(),
                    cliente.getId(),
                    cliente.getNombre() + " " + cliente.getApellido(),
                    reserva.getFecha(),
                    reserva.getHoraInicio(),
                    reserva.getHoraFin()
            );

            HttpEntity<RackReservaRequest> rackReservaBody = new HttpEntity<>(rackReservaRequest);

            try {
                restTemplate.postForEntity(RACK_RESERVA_ENDPOINT, rackReservaBody, String.class);

            } catch (HttpClientErrorException e) {
                throw new ReservaCreationException("Error del microservicio de Rack Semanal: " + e.getResponseBodyAsString(), e);
            } catch (HttpServerErrorException e) {
                throw new ReservaCreationException("Error interno en el microservicio de Rack Semanal: " + e.getResponseBodyAsString(), e);
            } catch (Exception e) {
                throw new ServiceIntegrationException("Error desconocido al integrar con Rack Semanal.", e);
            }
        }
    }


    public ReservasDiariasDTO contarReservasDiarias() {
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaAyer = fechaHoy.minusDays(1);

        return reservaRepository.countReservasConfirmadasOCompletadasDiarias(fechaHoy, fechaAyer);
    }

}
