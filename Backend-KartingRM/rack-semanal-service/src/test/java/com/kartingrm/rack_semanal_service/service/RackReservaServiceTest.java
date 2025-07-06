package com.kartingrm.rack_semanal_service.service;

import com.kartingrm.rack_semanal_service.dto.RackReservaDTO;
import com.kartingrm.rack_semanal_service.entity.RackReserva;
import com.kartingrm.rack_semanal_service.repository.RackReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RackReservaServiceTest {

    @Mock
    private RackReservaRepository rackReservaRepository;

    @InjectMocks
    private RackReservaService rackReservaService;

    private RackReserva rackReserva;
    private RackReservaDTO rackReservaDTO;

    @BeforeEach
    void setUp() {
        rackReserva = new RackReserva();
        rackReserva.setIdReserva(1L);
        rackReserva.setFecha(LocalDate.now());
        rackReserva.setHoraInicio(LocalTime.of(10, 0));
        rackReserva.setHoraFin(LocalTime.of(11, 0));
        rackReserva.setNombreReservante("John Doe");
        rackReserva.setIdCliente(100L);

        rackReservaDTO = new RackReservaDTO();
        rackReservaDTO.setFecha(LocalDate.now());
        rackReservaDTO.setHoraInicio(LocalTime.of(10, 0));
        rackReservaDTO.setHoraFin(LocalTime.of(11, 0));
        rackReservaDTO.setNombreReservante("John Doe");
        rackReservaDTO.setIdCliente(100L);
    }

    @Test
    void getRackReservas_ShouldReturnAllReservas() {
        when(rackReservaRepository.findAll()).thenReturn(List.of(rackReserva));

        List<RackReserva> result = rackReservaService.getRackReservas();

        assertEquals(1, result.size());
        assertEquals(rackReserva, result.get(0));
    }

    @Test
    void getRackReservaById_WhenExists_ShouldReturnReserva() {
        when(rackReservaRepository.findById(1L)).thenReturn(Optional.of(rackReserva));

        RackReserva result = rackReservaService.getRackReservaById(1L);

        assertEquals(rackReserva, result);
    }

    @Test
    void getRackReservaById_WhenNotExists_ShouldThrowException() {
        when(rackReservaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                rackReservaService.getRackReservaById(1L));
    }

    @Test
    void createRackReserva_WithValidDTO_ShouldReturnCreatedReserva() {
        when(rackReservaRepository.save(any(RackReserva.class))).thenReturn(rackReserva);

        RackReserva result = rackReservaService.createRackReserva(rackReservaDTO);

        assertNotNull(result);
        assertEquals(rackReservaDTO.getFecha(), result.getFecha());
        assertEquals(rackReservaDTO.getHoraInicio(), result.getHoraInicio());
        assertEquals(rackReservaDTO.getNombreReservante(), result.getNombreReservante());
    }

    @Test
    void createRackReserva_WithNullDTO_ShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () ->
                rackReservaService.createRackReserva(null));
    }

}