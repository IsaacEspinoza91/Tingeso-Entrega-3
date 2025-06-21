package com.kartingrm.reservas_comprobantes_service.service;

import com.kartingrm.reservas_comprobantes_service.entity.*;
import com.kartingrm.reservas_comprobantes_service.model.*;
import com.kartingrm.reservas_comprobantes_service.repository.ComprobanteRepository;
import com.kartingrm.reservas_comprobantes_service.repository.DetalleComprobanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
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
class ComprobanteServiceTest {

    @Mock
    private ComprobanteRepository comprobanteRepository;

    @Mock
    private DetalleComprobanteRepository detalleComprobanteRepository;

    @Mock
    private ReservaService reservaService;

    @Mock
    private ClienteReservaService clienteReservaService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ComprobanteService comprobanteService;

    private Comprobante comprobante;
    private Reserva reserva;
    private PlanDTO planDTO;
    private ClienteDTO clienteDTO;
    private DetalleComprobante detalleComprobante;
    private ClienteReserva clienteReserva;

    @BeforeEach
    void setUp() {
        // Configuración de objetos básicos
        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setIdReservante(1L);
        reserva.setIdPlan(1L);
        reserva.setFecha(LocalDate.now());
        reserva.setHoraInicio(LocalTime.of(15, 0));
        reserva.setTotalPersonas(3);

        comprobante = new Comprobante();
        comprobante.setId(1L);
        comprobante.setIdReserva(1L);
        comprobante.setPagado(true);

        planDTO = new PlanDTO();
        planDTO.setId(1L);
        planDTO.setPrecioRegular(100.0);
        planDTO.setPrecioFinSemana(150.0);
        planDTO.setPrecioFeriado(200.0);
        planDTO.setDuracionTotal(60);

        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Juan");
        clienteDTO.setApellido("Perez");

        detalleComprobante = new DetalleComprobante();
        detalleComprobante.setId(1L);
        detalleComprobante.setIdComprobante(1L);
        detalleComprobante.setIdCliente(1L);
        detalleComprobante.setMontoTotal(100.0);

        clienteReserva = new ClienteReserva();
        clienteReserva.setIdCliente(1L);
        clienteReserva.setIdReserva(1L);
    }

    @Test
    void getComprobanteConDetallesById_WhenExists_ShouldReturnComprobanteDTO() {
        // Arrange
        when(comprobanteRepository.findById(1L)).thenReturn(Optional.of(comprobante));
        when(reservaService.getReservaById(1L)).thenReturn(reserva);
        when(restTemplate.getForObject(anyString(), eq(ClienteDTO.class))).thenReturn(clienteDTO);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);
        when(detalleComprobanteRepository.findDetalleComprobantesByIdComprobante(1L))
                .thenReturn(List.of(detalleComprobante));

        // Act
        ComprobanteConDetallesDTO result = comprobanteService.getComprobanteConDetallesById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(comprobanteRepository, times(1)).findById(1L);
        verify(detalleComprobanteRepository, times(1)).findDetalleComprobantesByIdComprobante(1L);
    }

    @Test
    void getComprobanteConDetallesById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(comprobanteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            comprobanteService.getComprobanteConDetallesById(1L);
        });
    }

    @Test
    void getComprobanteConDetallesByIdReserva_WhenExists_ShouldReturnComprobanteDTO() {
        // Arrange
        when(comprobanteRepository.findByIdReserva(1L)).thenReturn(Optional.of(comprobante));
        when(reservaService.getReservaById(1L)).thenReturn(reserva);
        when(restTemplate.getForObject(anyString(), eq(ClienteDTO.class))).thenReturn(clienteDTO);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);
        when(detalleComprobanteRepository.findDetalleComprobantesByIdComprobante(1L))
                .thenReturn(List.of(detalleComprobante));

        // Act
        ComprobanteConDetallesDTO result = comprobanteService.getComprobanteConDetallesByIdReserva(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(comprobanteRepository, times(1)).findByIdReserva(1L);
    }


    @Test
    void createComprobante_WhenIntegrantesNotMatchTotalPersonas_ShouldThrowException() {
        // Arrange
        reserva.setTotalPersonas(3);
        when(reservaService.getReservaById(1L)).thenReturn(reserva);
        when(restTemplate.getForObject(contains("/planes/"), eq(PlanDTO.class))).thenReturn(planDTO);
        when(restTemplate.getForObject(contains("/cliente/"), eq(ClienteDTO.class))).thenReturn(clienteDTO);
        when(clienteReservaService.obtenerIntegrantesByIdReserva(1L)).thenReturn(List.of(clienteReserva));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            comprobanteService.createComprobante(1L, 0.0);
        });
    }

    @Test
    void createComprobante_WithValidData_ShouldReturnComprobanteDTO() {
        // Arrange
        reserva.setTotalPersonas(3);

        ClienteReserva integrante1 = new ClienteReserva();
        integrante1.setIdCliente(1L);
        integrante1.setIdReserva(1L);

        ClienteReserva integrante2 = new ClienteReserva();
        integrante2.setIdCliente(2L);
        integrante2.setIdReserva(1L);

        ClienteReserva integrante3 = new ClienteReserva();
        integrante3.setIdCliente(3L);
        integrante3.setIdReserva(1L);

        List<ClienteReserva> integrantes = List.of(integrante1, integrante2, integrante3);

        // Configurar comprobante con ID
        Comprobante comprobanteConId = new Comprobante();
        comprobanteConId.setId(1L);
        comprobanteConId.setIdReserva(1L);

        when(comprobanteRepository.findByIdReserva(1L)).thenReturn(Optional.empty());
        when(reservaService.getReservaById(1L)).thenReturn(reserva);
        when(restTemplate.getForObject(contains("/planes/"), eq(PlanDTO.class))).thenReturn(planDTO);
        when(restTemplate.getForObject(contains("/cliente/"), eq(ClienteDTO.class))).thenReturn(clienteDTO);
        when(clienteReservaService.obtenerIntegrantesByIdReserva(1L)).thenReturn(integrantes);
        when(restTemplate.getForObject(contains("/dias-feriados/"), eq(Boolean.class))).thenReturn(false);
        when(restTemplate.getForObject(contains("/cliente-cumpleanios/"), eq(Boolean.class))).thenReturn(false);
        when(comprobanteRepository.save(any(Comprobante.class))).thenReturn(comprobanteConId);

        // Mock para cada detalle
        when(detalleComprobanteRepository.save(any(DetalleComprobante.class)))
                .thenAnswer(invocation -> {
                    DetalleComprobante detalle = invocation.getArgument(0);
                    detalle.setId(1L); // Asignar ID simulado
                    return detalle;
                });

        // Mock para el cálculo de descuentos
        when(restTemplate.getForObject(contains("/desc-grupo/"), eq(Double.class))).thenReturn(0.1);
        when(restTemplate.getForObject(contains("/porcen-desc-by-visitas-cliente/"), eq(Double.class))).thenReturn(0.0);

        // Act
        ComprobanteConDetallesDTO result = comprobanteService.createComprobante(1L, 0.0);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(comprobanteRepository, times(1)).save(any(Comprobante.class));
        verify(detalleComprobanteRepository, times(3)).save(any(DetalleComprobante.class));
    }

    @Test
    void createComprobante_WithExistingComprobante_ShouldThrowException() {
        // Arrange
        when(comprobanteRepository.findByIdReserva(1L)).thenReturn(Optional.of(comprobante));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            comprobanteService.createComprobante(1L, 0.0);
        });
    }

    @Test
    void updateEstadoPagadoDeComprobante_WhenExists_ShouldUpdateAndReturnComprobante() {
        // Arrange
        when(comprobanteRepository.findById(1L)).thenReturn(Optional.of(comprobante));
        when(reservaService.getReservaById(1L)).thenReturn(reserva);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);
        when(comprobanteRepository.save(any(Comprobante.class))).thenReturn(comprobante);

        // Act
        Comprobante result = comprobanteService.updateEstadoPagadoDeComprobante(1L, false);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPagado());
        verify(comprobanteRepository, times(1)).save(comprobante);
    }

    @Test
    void deleteComprobante_WhenExists_ShouldReturnTrue() {
        // Arrange
        when(comprobanteRepository.findById(1L)).thenReturn(Optional.of(comprobante));
        when(reservaService.getReservaById(1L)).thenReturn(reserva);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);
        when(detalleComprobanteRepository.findDetalleComprobantesByIdComprobante(1L))
                .thenReturn(List.of(detalleComprobante));

        // Act
        boolean result = comprobanteService.deleteComprobante(1L);

        // Assert
        assertTrue(result);
        verify(comprobanteRepository, times(1)).deleteById(1L);
    }

    @Test
    void calcularTarifaBase_Weekday_ShouldReturnRegularPrice() {
        // Arrange
        LocalDate weekday = LocalDate.of(2023, 6, 14); // Miércoles
        reserva.setFecha(weekday);
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);

        // Act
        double result = comprobanteService.calcularTarifaBase(weekday, 1L);

        // Assert
        assertEquals(100.0, result);
    }

    @Test
    void calcularTarifaBase_Weekend_ShouldReturnWeekendPrice() {
        // Arrange
        LocalDate weekend = LocalDate.of(2023, 6, 17); // Sábado
        reserva.setFecha(weekend);
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);

        // Act
        double result = comprobanteService.calcularTarifaBase(weekend, 1L);

        // Assert
        assertEquals(150.0, result);
    }

    @Test
    void calcularTarifaBase_Holiday_ShouldReturnHolidayPrice() {
        // Arrange
        LocalDate holiday = LocalDate.of(2023, 6, 21); // Feriado (simulado)
        reserva.setFecha(holiday);
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);
        when(restTemplate.getForObject(anyString(), eq(PlanDTO.class))).thenReturn(planDTO);

        // Act
        double result = comprobanteService.calcularTarifaBase(holiday, 1L);

        // Assert
        assertEquals(200.0, result);
    }

    @Test
    void esCumpleaniero_WhenServiceAvailable_ShouldReturnBoolean() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);

        // Act
        boolean result = comprobanteService.esCumpleaniero(1L, LocalDate.now());

        // Assert
        assertTrue(result);
    }

    @Test
    void esCumpleaniero_WhenServiceUnavailable_ShouldThrowException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(Boolean.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        // Arrange
        LocalDate fecha = LocalDate.now();

        // Act & Assert
        assertThrows(HttpServerErrorException.class, () -> {
            comprobanteService.esCumpleaniero(1L, fecha);
        });
    }

    @Test
    void calcularDescuentoGrupo_WhenServiceAvailable_ShouldReturnDiscount() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(Double.class))).thenReturn(0.1);

        // Act
        double result = comprobanteService.calcularDescuentoGrupo(3);

        // Assert
        assertEquals(0.1, result);
    }

    @Test
    void determinarRangoPersonas_ShouldReturnCorrectRange() {
        assertEquals("1-2 personas", comprobanteService.determinarRangoPersonas(2));
        assertEquals("3-5 personas", comprobanteService.determinarRangoPersonas(4));
        assertEquals("6-10 personas", comprobanteService.determinarRangoPersonas(8));
        assertEquals("11-15 personas", comprobanteService.determinarRangoPersonas(12));
    }
}