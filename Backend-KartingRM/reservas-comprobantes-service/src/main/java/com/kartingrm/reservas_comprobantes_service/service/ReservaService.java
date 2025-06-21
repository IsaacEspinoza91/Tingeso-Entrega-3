package com.kartingrm.reservas_comprobantes_service.service;

import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.*;
import com.kartingrm.reservas_comprobantes_service.modelbase.BaseService;
import com.kartingrm.reservas_comprobantes_service.repository.ReservaRepository;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
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

    static final String URL_CLIENTE_DESC_FRECU_MS = "http://cliente-desc-frecu-service";
    static final String CLIENTE_DESC_FRECU_BASE = "/api/cliente-service";
    static final String CLIENTE_ENDPOINT = URL_CLIENTE_DESC_FRECU_MS + CLIENTE_DESC_FRECU_BASE + "/cliente/";

    static final String URL_PLAN_MS = "http://plan-service";
    static final String PLAN_BASE = "/api/plan-service";
    static final String PLAN_ENDPOINT = URL_PLAN_MS + PLAN_BASE + "/planes/";

    static final String URL_DIAS_ESPECIALES_MS = "http://dias-especiales-service";
    static final String DIAS_ESPECIALES_BASE = "/api/dias-especiales-service";
    static final String DIAS_FERIADOS_ENDPOINT = URL_DIAS_ESPECIALES_MS + DIAS_ESPECIALES_BASE + "/dias-feriados/";

    static final String URL_RACK_SEMANAL_MS = "http://rack-semanal-service";
    static final String RACK_SEMANAL_BASE = "/api/rack-semanal-service";
    static final String RACK_SEMANAL_ENDPOINT = URL_RACK_SEMANAL_MS + RACK_SEMANAL_BASE + "/rack-reserva/";



    private final ReservaRepository reservaRepository;
    public ReservaService(ReservaRepository reservaRepository, RestTemplate restTemplate) {
        super(restTemplate);
        this.reservaRepository = reservaRepository;
    }


    public List<Reserva> getReservas() {
        return reservaRepository.findAll();
    }

    public List<ReservaDTO> getReservasDTO() {
        List<Reserva> reservas = reservaRepository.findAll();
        List<ReservaDTO> reservasDTO = new ArrayList<>();
        for (Reserva reservaActual : reservas) {
            ClienteDTO cliente = obtenerCliente(reservaActual.getIdReservante());
            PlanDTO plan = obtenerPlan(reservaActual.getIdPlan());

            ReservaDTO reservaDTO = new ReservaDTO(reservaActual, plan, cliente);

            reservasDTO.add(reservaDTO);
        }
        return reservasDTO;
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
        List<ReservaDTO> reservasDTO = new ArrayList<>();
        for (Reserva reservaActual : reservas) {
            ClienteDTO cliente = obtenerCliente(reservaActual.getIdReservante());
            PlanDTO plan = obtenerPlan(reservaActual.getIdPlan());

            ReservaDTO reservaDTO = new ReservaDTO(reservaActual, plan, cliente);

            reservasDTO.add(reservaDTO);
        }
        return reservasDTO;
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

                restTemplate.postForObject(RACK_SEMANAL_ENDPOINT, rackReservaBody, RackReservaRequest.class);

            }
            // Else, Caso mantiene estado era completada o confirmada. No realiza petición http
        } else {// Estado nuevo es cancelada
            // Caso estado anterior era completada o confirmada
            if (estadoAnterior.equals(ESTADO_COMPLETADA) || estadoAnterior.equals(ESTADO_CONFIRMADA)) {
                restTemplate.delete(RACK_SEMANAL_ENDPOINT + reserva.getId());
            }
            // Else, Caso mantiene estado cancelada. No hace petición http
        }

        return reserva;
    }



    public boolean deleteReserva(Long id) {
        Optional<Reserva> reservaOriginalOptional = reservaRepository.findById(id);
        if (reservaOriginalOptional.isEmpty()) throw new EntityNotFoundException("Reserva id " + id + " no encontrado");

        reservaRepository.deleteById(id);
        restTemplate.delete(RACK_SEMANAL_ENDPOINT + id);
        return true;
    }



    //  Métodos privados. Lógica interna

    // Obtener reservas existentes entre dos horas de un día. Condición para crear reservas
    private boolean existeReservaEntreDosHoras(LocalDate fecha, LocalTime horaInicio, LocalTime horaFinal) {
        return reservaRepository.existeReservaEntreDosHoras(fecha, horaInicio, horaFinal);
    }

    // Validación si reserva es en horario de trabajo
    //  lunes a viernes: 14:00 a 22:00
    //  sábados, domingos, feriados: 10:00 a 22:00
    private void esHorarioValido(Boolean diaFeriado, Boolean diaFinDeSemana, Reserva reserva) {
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
    private LocalTime determinarHoraFin(Reserva reserva, PlanDTO plan) {
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

            restTemplate.postForObject(RACK_SEMANAL_ENDPOINT, rackReservaBody, RackReservaRequest.class);
        }
    }



}
