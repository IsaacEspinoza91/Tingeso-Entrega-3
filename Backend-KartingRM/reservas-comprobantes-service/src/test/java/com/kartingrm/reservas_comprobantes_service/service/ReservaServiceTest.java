package com.kartingrm.reservas_comprobantes_service.service;

import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.*;
import com.kartingrm.reservas_comprobantes_service.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ReservaService reservaService;

    private Reserva reserva;
    private ReservaRequest reservaRequest;
    private ClienteDTO clienteDTO;
    private PlanDTO planDTO;

    @BeforeEach
    void setUp() {
        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setIdReservante(1L);
        reserva.setIdPlan(1L);
        reserva.setFecha(LocalDate.now());
        reserva.setHoraInicio(LocalTime.of(15, 0));
        reserva.setEstado("confirmada");

        reservaRequest = new ReservaRequest();
        reservaRequest.setIdReservante(1L);
        reservaRequest.setIdPlan(1L);
        reservaRequest.setFecha(LocalDate.now());
        reservaRequest.setHoraInicio(LocalTime.of(15, 0));
        reservaRequest.setEstado("confirmada");

        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Juan");
        clienteDTO.setApellido("Perez");

        planDTO = new PlanDTO();
        planDTO.setId(1L);
        planDTO.setDuracionTotal(60); // 60 minutos
        
    }

    @Test
    void getReservas_ShouldReturnAllReservas() {
        // Arrange
        List<Reserva> expectedReservas = List.of(reserva);
        when(reservaRepository.findAll()).thenReturn(expectedReservas);

        // Act
        List<Reserva> result = reservaService.getReservas();

        // Assert
        assertEquals(expectedReservas, result);
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    void getReservasDTO_ShouldReturnAllReservasDTO() {
        // Arrange
        List<Reserva> reservas = List.of(reserva);
        when(reservaRepository.findAll()).thenReturn(reservas);
        when(restTemplate.getForObject(anyString(), eq(ClienteDTO.class))).thenReturn(clienteDTO);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);

        // Act
        List<ReservaDTO> result = reservaService.getReservasDTO();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reserva.getId(), result.get(0).getId());
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    void getReservaById_WhenExists_ShouldReturnReserva() {
        // Arrange
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act
        Reserva result = reservaService.getReservaById(1L);

        // Assert
        assertEquals(reserva, result);
        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    void getReservaById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            reservaService.getReservaById(1L);
        });
        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    void getReservaDTOById_WhenExists_ShouldReturnReservaDTO() {
        // Arrange
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(restTemplate.getForObject(anyString(), eq(ClienteDTO.class))).thenReturn(clienteDTO);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);

        // Act
        ReservaDTO result = reservaService.getReservaDTOById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(reserva.getId(), result.getId());
        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    void getReservasByIdReservante_ShouldReturnReservasForCliente() {
        // Arrange
        List<Reserva> expectedReservas = List.of(reserva);
        when(reservaRepository.findReservasByIdReservante(1L)).thenReturn(expectedReservas);

        // Act
        List<Reserva> result = reservaService.getReservasByIdReservante(1L);

        // Assert
        assertEquals(expectedReservas, result);
        verify(reservaRepository, times(1)).findReservasByIdReservante(1L);
    }

    @Test
    void getReservasDTOByIdReservante_ShouldReturnReservasDTOForCliente() {
        // Arrange
        List<Reserva> reservas = List.of(reserva);
        when(reservaRepository.findReservasByIdReservante(1L)).thenReturn(reservas);
        when(restTemplate.getForObject(anyString(), eq(ClienteDTO.class))).thenReturn(clienteDTO);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);

        // Act
        List<ReservaDTO> result = reservaService.getReservasDTOByIdReservante(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reserva.getId(), result.get(0).getId());
        verify(reservaRepository, times(1)).findReservasByIdReservante(1L);
    }

    @Test
    void createReserva_WithValidData_ShouldSaveAndReturnReserva() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(ClienteDTO.class))).thenReturn(clienteDTO);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);
        when(reservaRepository.existeReservaEntreDosHoras(any(LocalDate.class), any(LocalTime.class), any(LocalTime.class)))
                .thenReturn(false);
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // Act
        Reserva result = reservaService.createReserva(reservaRequest);

        // Assert
        assertNotNull(result);
        assertEquals(reservaRequest.getHoraInicio(), result.getHoraInicio());
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void createReserva_WithExistingReservation_ShouldThrowException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(ClienteDTO.class))).thenReturn(clienteDTO);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);
        when(reservaRepository.existeReservaEntreDosHoras(any(LocalDate.class), any(LocalTime.class), any(LocalTime.class)))
                .thenReturn(true);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            reservaService.createReserva(reservaRequest);
        });
    }

    @Test
    void createReserva_WithInvalidTime_ShouldThrowException() {
        // Arrange
        reservaRequest.setHoraInicio(LocalTime.of(8, 0)); // Hora inválida
        when(restTemplate.getForObject(anyString(), eq(ClienteDTO.class))).thenReturn(clienteDTO);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);
        when(reservaRepository.existeReservaEntreDosHoras(any(LocalDate.class), any(LocalTime.class), any(LocalTime.class)))
                .thenReturn(false);
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            reservaService.createReserva(reservaRequest);
        });
    }

    @Test
    void updateReserva_WhenExists_ShouldUpdateAndReturnReserva() {
        // Arrange
        Reserva reservaOriginal = new Reserva();
        reservaOriginal.setEstado("cancelada");

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaOriginal));
        when(restTemplate.getForObject(anyString(), eq(ClienteDTO.class))).thenReturn(clienteDTO);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // Act
        Reserva result = reservaService.updateReserva(1L, reservaRequest);

        // Assert
        assertNotNull(result);
        assertEquals(reservaRequest.getEstado(), result.getEstado());
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void updateReserva_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            reservaService.updateReserva(1L, reservaRequest);
        });
    }

    @Test
    void deleteReserva_WhenExists_ShouldReturnTrue() {
        // Arrange
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act
        boolean result = reservaService.deleteReserva(1L);

        // Assert
        assertTrue(result);
        verify(reservaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteReserva_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            reservaService.deleteReserva(1L);
        });
    }

    @Test
    void determinarHoraFin_ShouldCalculateCorrectEndTime() {
        // Arrange
        PlanDTO plan = new PlanDTO();
        plan.setDuracionTotal(90); // 1 hora y 30 minutos
        reserva.setHoraInicio(LocalTime.of(14, 0));

        // Act
        LocalTime result = reservaService.determinarHoraFin(reserva, plan);

        // Assert
        assertEquals(LocalTime.of(15, 30), result);
    }

    @Test
    void esHorarioValido_WeekdayValidTime_ShouldNotThrowException() {
        // Arrange
        reserva.setHoraInicio(LocalTime.of(14, 0));
        reserva.setHoraFin(LocalTime.of(15, 0)); // Añadir horaFin

        // Act & Assert
        assertDoesNotThrow(() -> {
            reservaService.esHorarioValido(false, false, reserva);
        });
    }

    @Test
    void esHorarioValido_WeekdayInvalidTime_ShouldThrowException() {
        // Arrange
        reserva.setHoraInicio(LocalTime.of(8, 0));
        reserva.setHoraFin(LocalTime.of(9, 0)); // Añadir horaFin

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            reservaService.esHorarioValido(false, false, reserva);
        });
    }

    @Test
    void esHorarioValido_WeekendValidTime_ShouldNotThrowException() {
        // Arrange
        reserva.setHoraInicio(LocalTime.of(10, 0));
        reserva.setHoraFin(LocalTime.of(11, 0)); // Añadir horaFin

        // Act & Assert
        assertDoesNotThrow(() -> {
            reservaService.esHorarioValido(false, true, reserva);
        });
    }
}