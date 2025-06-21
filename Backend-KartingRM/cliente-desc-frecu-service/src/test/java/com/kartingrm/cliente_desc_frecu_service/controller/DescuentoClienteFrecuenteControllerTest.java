package com.kartingrm.cliente_desc_frecu_service.controller;

import com.kartingrm.cliente_desc_frecu_service.dto.DescuentoClienteFrecuenteDTO;
import com.kartingrm.cliente_desc_frecu_service.entity.DescuentoClienteFrecuente;
import com.kartingrm.cliente_desc_frecu_service.service.DescuentoClienteFrecuenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DescuentoClienteFrecuenteControllerTest {

    @Mock
    private DescuentoClienteFrecuenteService descuentoClienteFrecuenteService;

    @InjectMocks
    private DescuentoClienteFrecuenteController descuentoClienteFrecuenteController;

    private DescuentoClienteFrecuente descuento;
    private DescuentoClienteFrecuenteDTO descuentoDTO;

    @BeforeEach
    void setUp() {
        descuento = new DescuentoClienteFrecuente();
        descuento.setId(1L);
        descuento.setMaxReservas(5);
        descuento.setMinReservas(3);
        descuento.setPorcentajeDescuento(10.0);

        descuentoDTO = new DescuentoClienteFrecuenteDTO();
        descuentoDTO.setMaxReservas(5);
        descuentoDTO.setMinReservas(3);
        descuentoDTO.setPorcentajeDescuento(10.0);
    }

    @Test
    void getDescuentosClienteFrecuente_ShouldReturnAllDescuentos() {
        when(descuentoClienteFrecuenteService.getDescuentosClienteFrecuente()).thenReturn(List.of(descuento));

        ResponseEntity<List<DescuentoClienteFrecuente>> response =
                descuentoClienteFrecuenteController.getDescuentosClienteFrecuente();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(descuento, response.getBody().get(0));
    }

    @Test
    void getDescuentoClienteFrecuenteById_ShouldReturnDescuento() {
        when(descuentoClienteFrecuenteService.getDescuentoClienteFrecuenteById(1L)).thenReturn(descuento);

        ResponseEntity<DescuentoClienteFrecuente> response =
                descuentoClienteFrecuenteController.getDescuentoClienteFrecuenteById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(descuento, response.getBody());
    }

    @Test
    void createDescuentoClienteFrecuente_ShouldReturnCreatedDescuento() {
        when(descuentoClienteFrecuenteService.createDescuentoClienteFrecuente(descuentoDTO)).thenReturn(descuento);

        ResponseEntity<DescuentoClienteFrecuente> response =
                descuentoClienteFrecuenteController.createDescuentoClienteFrecuente(descuentoDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(descuento, response.getBody());
    }

    @Test
    void updateDescuentoClienteFrecuente_ShouldReturnUpdatedDescuento() {
        when(descuentoClienteFrecuenteService.updateDescuentoClienteFrecuente(1L, descuentoDTO)).thenReturn(descuento);

        ResponseEntity<DescuentoClienteFrecuente> response =
                descuentoClienteFrecuenteController.updateDescuentoClienteFrecuente(1L, descuentoDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(descuento, response.getBody());
    }

    @Test
    void deleteDescuentoClienteFrecuente_ShouldReturnTrueWhenSuccess() {
        when(descuentoClienteFrecuenteService.deleteDescuentoClienteFrecuente(1L)).thenReturn(true);

        ResponseEntity<Boolean> response =
                descuentoClienteFrecuenteController.deleteDescuentoClienteFrecuente(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void getPorcentajeDescuentoGrupoByCantidadIntegrantes_ShouldReturnPorcentaje() {
        when(descuentoClienteFrecuenteService.getPorcentajeDescuentoClienteFrecuenteByCantidadVisitas(5))
                .thenReturn(10.0);

        ResponseEntity<Double> response =
                descuentoClienteFrecuenteController.getPorcentajeDescuentoGrupoByCantidadIntegrantes(5);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(10.0, response.getBody());
    }

    // Pruebas para casos de error (opcional)
    @Test
    void getDescuentoClienteFrecuenteById_WhenNotExists_ShouldThrowException() {
        when(descuentoClienteFrecuenteService.getDescuentoClienteFrecuenteById(99L))
                .thenThrow(new EntityNotFoundException("Descuento no encontrado"));

        assertThrows(EntityNotFoundException.class,
                () -> descuentoClienteFrecuenteController.getDescuentoClienteFrecuenteById(99L));
    }
}