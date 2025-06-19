package com.kartingrm.reservas_comprobantes_service.service;

import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.ClienteDTO;
import com.kartingrm.reservas_comprobantes_service.model.PlanDTO;
import com.kartingrm.reservas_comprobantes_service.model.RackReservaRequest;
import com.kartingrm.reservas_comprobantes_service.model.ReservaDTO;
import com.kartingrm.reservas_comprobantes_service.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    RestTemplate restTemplate;


    public List<Reserva> getReservas() {
        return reservaRepository.findAll();
    }

    public List<ReservaDTO> getReservasDTO() {
        List<Reserva> reservas = reservaRepository.findAll();
        List<ReservaDTO> reservasDTO = new ArrayList<>();
        for (Reserva reservaActual : reservas) {

            // Obtener Cliente
            ClienteDTO cliente = restTemplate.getForObject("http://CLIENTEDESCFRECU/api/cliente-service/cliente/" + reservaActual.getIdReservante(), ClienteDTO.class);
            // Obtener Plan
            PlanDTO plan = restTemplate.getForObject("http://PLAN/api/plan/planes/" + reservaActual.getIdPlan(), PlanDTO.class);

            // Crear DTO de respuesta. Patron Builder
            ReservaDTO reservaDTO = new ReservaDTO();
            reservaDTO.setId(reservaActual.getId());
            reservaDTO.setFecha(reservaActual.getFecha());
            reservaDTO.setHoraInicio(reservaActual.getHoraInicio());
            reservaDTO.setHoraFin(reservaActual.getHoraFin());
            reservaDTO.setEstado(reservaActual.getEstado());
            reservaDTO.setTotalPersonas(reservaActual.getTotalPersonas());
            reservaDTO.setPlan(plan);
            reservaDTO.setReservante(cliente);


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

        // Obtener Cliente
        ClienteDTO cliente = restTemplate.getForObject("http://CLIENTEDESCFRECU/api/cliente-service/cliente/" + reserva.get().getIdReservante(), ClienteDTO.class);
        // Obtener Plan
        PlanDTO plan = restTemplate.getForObject("http://PLAN/api/plan/planes/" + reserva.get().getIdPlan(), PlanDTO.class);

        // Crear DTO de respuesta. Patron Builder
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setId(reserva.get().getId());
        reservaDTO.setFecha(reserva.get().getFecha());
        reservaDTO.setHoraInicio(reserva.get().getHoraInicio());
        reservaDTO.setHoraFin(reserva.get().getHoraFin());
        reservaDTO.setEstado(reserva.get().getEstado());
        reservaDTO.setTotalPersonas(reserva.get().getTotalPersonas());
        reservaDTO.setPlan(plan);
        reservaDTO.setReservante(cliente);

        return reservaDTO;
    }

    // Obtener todas las reservas de un cliente segun id
    public List<Reserva> getReservasByIdReservante(Long idCliente) {
        return reservaRepository.findReservasByIdReservante(idCliente);
    }


    // Obtener todas las reservas DTO de un cliente segun id
    public List<ReservaDTO> getReservasDTOByIdReservante(Long idCliente) {
        List<Reserva> reservas = reservaRepository.findReservasByIdReservante(idCliente);
        List<ReservaDTO> reservasDTO = new ArrayList<>();
        for (Reserva reservaActual : reservas) {

            // Obtener Cliente
            ClienteDTO cliente = restTemplate.getForObject("http://CLIENTEDESCFRECU/api/cliente-service/cliente/" + reservaActual.getIdReservante(), ClienteDTO.class);
            // Obtener Plan
            PlanDTO plan = restTemplate.getForObject("http://PLAN/api/plan/planes/" + reservaActual.getIdPlan(), PlanDTO.class);

            // Crear DTO de respuesta. Patron Builder
            ReservaDTO reservaDTO = new ReservaDTO();
            reservaDTO.setId(reservaActual.getId());
            reservaDTO.setFecha(reservaActual.getFecha());
            reservaDTO.setHoraInicio(reservaActual.getHoraInicio());
            reservaDTO.setHoraFin(reservaActual.getHoraFin());
            reservaDTO.setEstado(reservaActual.getEstado());
            reservaDTO.setTotalPersonas(reservaActual.getTotalPersonas());
            reservaDTO.setPlan(plan);
            reservaDTO.setReservante(cliente);


            reservasDTO.add(reservaDTO);
        }
        return reservasDTO;
    }


    // Crear reserva
    // Considera que no existan reservas previas en el horario, ademas ingresa automaticamente la hora de fin segun el plan
    // El cuerpo ya tiene id cliente reservante e id plan
    public Reserva createReserva(Reserva reserva) {
        // Obtener Cliente
        ClienteDTO cliente = restTemplate.getForObject("http://CLIENTEDESCFRECU/api/cliente-service/cliente/" + reserva.getIdReservante(), ClienteDTO.class);
        // Obtener Plan
        PlanDTO plan = restTemplate.getForObject("http://PLAN/api/plan/planes/" + reserva.getIdPlan(), PlanDTO.class);

        // Verificar parametros validos
        if (reserva.getHoraInicio() != null && cliente != null && plan != null) {

            // Determinar hora de fin sumando minutos del plan a la hora inicial
            LocalTime horaInicio = reserva.getHoraInicio();
            LocalTime horaFinalCalculada = horaInicio.plusMinutes(plan.getDuracionTotal());
            reserva.setHoraFin(horaFinalCalculada);

            // Verificar que no existan reservas enter ambas horas
            if (existeReservaEntreDosHoras(reserva.getFecha(), horaInicio, horaFinalCalculada))
                throw new IllegalStateException("Ya existe una reserva con ese horario");



            // Verificar Horario de inicio y fin validos.
            //  Lunes a Viernes: 14:00 a 22:00
            //  Sabados, Domingos, Feriados: 10:00 a 22:00
            boolean esFinDeSemana = reserva.getFecha().getDayOfWeek().getValue() >= 6;// Determina fin de semana o no

            // Obtener bool si es feriado
            boolean esFeriado = restTemplate.getForObject("http://DIASESPECIALES/api/dias-especiales-service/dias-feriados/esFeriado?fecha=" + reserva.getFecha(),Boolean.class);
            if (esFeriado || esFinDeSemana){// Horario fin de semana o feriado
                // Horario antes o despues del horario de servicio
                if (horaInicio.isBefore(LocalTime.of(10,00,00)) || horaFinalCalculada.isAfter(LocalTime.of(22,00,00))) {
                    throw new IllegalStateException("Horario incorrecto. Domingos, sábados y feriados: 10:00 a 22:00");
                }
            } else { // Horario semana
                if (horaInicio.isBefore(LocalTime.of(14,00,00)) || horaFinalCalculada.isAfter(LocalTime.of(22,00,00))) {
                    throw new IllegalStateException("Horario incorrecto. Lunes a Viernes: 14:00 a 22:00");
                }
            }

            // Se guarda reserva en la base de datos
            reservaRepository.save(reserva);


            // Guardar relacion de reserva en la tabla rack_reserva, para obtener el rack semanal desde el MC6
            // Solo en caso de reserva confirmada o completada
            if (reserva.getEstado().equals("completada") || reserva.getEstado().equals("confirmada")) {
                RackReservaRequest rackReservaRequest = new RackReservaRequest(
                        reserva.getId(),
                        cliente.getId(),
                        cliente.getNombre() + cliente.getApellido(),
                        reserva.getFecha(),
                        reserva.getHoraInicio(),
                        reserva.getHoraFin()
                );

                HttpEntity<RackReservaRequest> rackReservaBody = new HttpEntity<>(rackReservaRequest);

                RackReservaRequest respuesta = restTemplate.postForObject(
                        "http://RACKSEMANAL/api/rack-semanal-service/rack-reserva/",
                        rackReservaBody,
                        RackReservaRequest.class
                );
            }

            return reserva;
        } else {
            throw new RuntimeException("Cliente no encontrado, plan no existe, o hora errónea");
        }
    }


    // Update de valor de reserva. Permite cambio de plan y de cliente reservante
    public Reserva updateReserva(Long id, Reserva reserva) {
        Optional<Reserva> reservaOriginalOptional = reservaRepository.findById(id);
        if (reservaOriginalOptional.isEmpty()) throw new EntityNotFoundException("Reserva id " + id + " no encontrado");

        String estadoAnterior = reservaOriginalOptional.get().getEstado();
        // Reserva actualizada conserva id de la reserva original
        reserva.setId(id);

        // Calculo de hora final segun nueva hora o plan
        // Obtener Plan
        PlanDTO plan = restTemplate.getForObject("http://PLAN/api/plan/planes/" + reserva.getIdPlan(), PlanDTO.class);
        // Obtener Cliente
        ClienteDTO cliente = restTemplate.getForObject("http://CLIENTEDESCFRECU/api/cliente-service/cliente/" + reserva.getIdReservante(), ClienteDTO.class);

        // Solo permite actualizar la hora inicial
        // Determinar hora de fin sumando minutos del plan a la hora inicial
        LocalTime horaInicio = reserva.getHoraInicio();
        LocalTime horaFinalCalculada = horaInicio.plusMinutes(plan.getDuracionTotal());
        reserva.setHoraFin(horaFinalCalculada);


        // Verificar horario valido de atencion
        boolean esFinDeSemana = reserva.getFecha().getDayOfWeek().getValue() >= 6;// Determina fin de semana o no
        boolean esFeriado = restTemplate.getForObject("http://DIASESPECIALES/api/dias-especiales-service/dias-feriados/esFeriado?fecha=" + reserva.getFecha(),Boolean.class);
        if (esFeriado || esFinDeSemana){// Horario fin de semana o feriado
            // Horario antes o despues del horario de servicio
            if (horaInicio.isBefore(LocalTime.of(10,00,00)) || horaFinalCalculada.isAfter(LocalTime.of(22,00,00))) {
                throw new IllegalStateException("Horario incorrecto. Domingos, sábados y feriados: 10:00 a 22:00");
            }
        } else { // Horario semana
            if (horaInicio.isBefore(LocalTime.of(14,00,00)) || horaFinalCalculada.isAfter(LocalTime.of(22,00,00))) {
                throw new IllegalStateException("Horario incorrecto. Lunes a Viernes: 14:00 a 22:00");
            }
        }

        // Se guarda reserva en base de datos
        reservaRepository.save(reserva);

        // Guardar relacion de reserva en la tabla rack_reserva, para obtener el rack semanal desde el MC6
        // Estado nuevo es completada o confirmada
        if (reserva.getEstado().equals("completada") || reserva.getEstado().equals("confirmada")) {
            // Estado anterior era cancelada
            if (!estadoAnterior.equals("completada") && !estadoAnterior.equals("confirmada")) {

                RackReservaRequest rackReservaRequest = new RackReservaRequest(
                        reserva.getId(),
                        cliente.getId(),
                        cliente.getNombre() + " " + cliente.getApellido(),
                        reserva.getFecha(),
                        reserva.getHoraInicio(),
                        reserva.getHoraFin()
                );

                HttpEntity<RackReservaRequest> rackReservaBody = new HttpEntity<>(rackReservaRequest);

                RackReservaRequest respuesta = restTemplate.postForObject(
                        "http://RACKSEMANAL/api/rack-semanal-service/rack-reserva/",
                        rackReservaBody,
                        RackReservaRequest.class
                );

            }
            // Else, Caso mantiene estado era completada o confirmada. No realiza peticion http
        } else {// Estado nuevo es cancelada
            // Caso estado anterior era completada o confirmada
            if (estadoAnterior.equals("completada") || estadoAnterior.equals("confirmada")) {
                restTemplate.delete("http://RACKSEMANAL/api/rack-semanal-service/rack-reserva/" + reserva.getId());
            }
            // Else, Caso mantiene estado cancelada. No hace peticion http
        }

        return reserva;
    }


    public boolean deleteReserva(Long id) {
        Optional<Reserva> reservaOriginalOptional = reservaRepository.findById(id);
        if (reservaOriginalOptional.isEmpty()) throw new EntityNotFoundException("Reserva id " + id + " no encontrado");

        reservaRepository.deleteById(id);
        restTemplate.delete("http://RACKSEMANAL/api/rack-semanal-service/rack-reserva/" + id);
        return true;
    }



    //  Metodos privados. Logica interna

    // Obtener reservas existentes entre dos horas de un dia
    // Se usa en como condicion para crear reservas
    private boolean existeReservaEntreDosHoras(LocalDate fecha, LocalTime horaInicio, LocalTime horaFinal) {
        return reservaRepository.existeReservaEntreDosHoras(fecha, horaInicio, horaFinal);
    }


    // Metodos para obtener info para reportes

}
