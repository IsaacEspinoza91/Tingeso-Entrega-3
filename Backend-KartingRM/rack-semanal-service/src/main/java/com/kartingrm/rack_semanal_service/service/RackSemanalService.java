package com.kartingrm.rack_semanal_service.service;

import com.kartingrm.rack_semanal_service.DTO.ReservaInfo;
import com.kartingrm.rack_semanal_service.DTO.ReservaSemanalResponse;
import com.kartingrm.rack_semanal_service.DTO.ReservasPorDia;
import com.kartingrm.rack_semanal_service.entity.RackReserva;
import com.kartingrm.rack_semanal_service.repository.RackReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RackSemanalService {

    private final RackReservaRepository reservaRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    public RackSemanalService(RackReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public ReservaSemanalResponse obtenerReservasSemana(int semanaOffset) {
        // Determinar fecha actual
        LocalDate hoy = LocalDate.now();
        // Determinar lunes inicio de semana
        LocalDate inicioSemana = hoy.with(DayOfWeek.MONDAY).plusWeeks(semanaOffset);
        // Deteminar domingo fin de semana
        LocalDate finSemana = inicioSemana.plusDays(6);

        // Obtener reservas (tabla rack_semana) entre el inicio y fin de semana
        List<RackReserva> reservas = reservaRepository.findByFechaBetween(inicioSemana, finSemana);

        // Creacion de diccionario agrupado segun la fecha. Key (fecha), Value (lista reservas)
        Map<LocalDate, List<RackReserva>> reservasPorFecha = reservas.stream()
                .collect(Collectors.groupingBy(RackReserva::getFecha));

        // Creacion de listas para reservas por cada dia. Patron Builder
        ReservasPorDia reservasPorDia = new ReservasPorDia();
        reservasPorDia.setLunes(convertirReservas(reservasPorFecha.get(inicioSemana)));
        reservasPorDia.setMartes(convertirReservas(reservasPorFecha.get(inicioSemana.plusDays(1))));
        reservasPorDia.setMiercoles(convertirReservas(reservasPorFecha.get(inicioSemana.plusDays(2))));
        reservasPorDia.setJueves(convertirReservas(reservasPorFecha.get(inicioSemana.plusDays(3))));
        reservasPorDia.setViernes(convertirReservas(reservasPorFecha.get(inicioSemana.plusDays(4))));
        reservasPorDia.setSabado(convertirReservas(reservasPorFecha.get(inicioSemana.plusDays(5))));
        reservasPorDia.setDomingo(convertirReservas(reservasPorFecha.get(inicioSemana.plusDays(6))));

        // Creacion de respuesta para retornar, se agrega el objeto reservasPorDia con las listas de reservas c/u dia
        ReservaSemanalResponse response = new ReservaSemanalResponse();
        response.setFechaInicioSemana(inicioSemana.format(dateFormatter));
        response.setFechaFinSemana(finSemana.format(dateFormatter));
        response.setReservasPorDia(reservasPorDia);

        return response;
    }



    private List<ReservaInfo> convertirReservas(List<RackReserva> reservas) {
        // Condicion no hay reservas para el dia
        if (reservas == null) return Collections.emptyList();

        // Retorno lista con los elementos reservaInfo del dia
        return reservas.stream()
                .map(r -> new ReservaInfo(
                        r.getIdReserva(),
                        r.getNombreReservante(),
                        r.getHoraInicio().format(timeFormatter),
                        r.getHoraFin().format(timeFormatter)))
                .collect(Collectors.toList());
    }

}