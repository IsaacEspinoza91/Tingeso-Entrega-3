package com.kartingrm.cliente_desc_frecu_service.service;

import com.kartingrm.cliente_desc_frecu_service.dto.DescuentoClienteFrecuenteDTO;
import com.kartingrm.cliente_desc_frecu_service.entity.DescuentoClienteFrecuente;
import com.kartingrm.cliente_desc_frecu_service.repository.DescuentoClienteFrecuenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DescuentoClienteFrecuenteServiceTest {

    @Mock
    private DescuentoClienteFrecuenteRepository descuentoRepository;

    @InjectMocks
    private DescuentoClienteFrecuenteService descuentoService;

    private DescuentoClienteFrecuente descuento;
    private DescuentoClienteFrecuenteDTO descuentoDTO;

    @BeforeEach
    void setUp() {
        descuento = new DescuentoClienteFrecuente();
        descuento.setId(1L);
        descuento.setMinReservas(5);
        descuento.setMaxReservas(10);
        descuento.setPorcentajeDescuento(15.0);

        descuentoDTO = new DescuentoClienteFrecuenteDTO();
        descuentoDTO.setMinReservas(5);
        descuentoDTO.setMaxReservas(10);
        descuentoDTO.setPorcentajeDescuento(15.0);
    }

    @Test
    void getDescuentosClienteFrecuente_ShouldReturnAllDescuentos() {
        when(descuentoRepository.findAll()).thenReturn(List.of(descuento));

        List<DescuentoClienteFrecuente> result = descuentoService.getDescuentosClienteFrecuente();

        assertEquals(1, result.size());
        assertEquals(descuento, result.get(0));
    }

    @Test
    void getDescuentoClienteFrecuenteById_WhenExists_ShouldReturnDescuento() {
        when(descuentoRepository.findById(1L)).thenReturn(Optional.of(descuento));

        DescuentoClienteFrecuente result = descuentoService.getDescuentoClienteFrecuenteById(1L);

        assertEquals(descuento, result);
    }

    @Test
    void getDescuentoClienteFrecuenteById_WhenNotExists_ShouldThrowException() {
        when(descuentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                descuentoService.getDescuentoClienteFrecuenteById(1L));
    }

    @Test
    void createDescuentoClienteFrecuente_WithValidDTO_ShouldReturnCreatedDescuento() {
        when(descuentoRepository.save(any(DescuentoClienteFrecuente.class))).thenReturn(descuento);

        DescuentoClienteFrecuente result = descuentoService.createDescuentoClienteFrecuente(descuentoDTO);

        assertEquals(descuentoDTO.getMinReservas(), result.getMinReservas());
        assertEquals(descuentoDTO.getMaxReservas(), result.getMaxReservas());
        assertEquals(descuentoDTO.getPorcentajeDescuento(), result.getPorcentajeDescuento());
    }

    @Test
    void createDescuentoClienteFrecuente_WithNullDTO_ShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () ->
                descuentoService.createDescuentoClienteFrecuente(null));
    }

    @Test
    void updateDescuentoClienteFrecuente_WhenExists_ShouldReturnUpdatedDescuento() {
        when(descuentoRepository.findById(1L)).thenReturn(Optional.of(descuento));
        when(descuentoRepository.save(any(DescuentoClienteFrecuente.class))).thenReturn(descuento);

        DescuentoClienteFrecuente result = descuentoService.updateDescuentoClienteFrecuente(1L, descuentoDTO);

        assertEquals(descuentoDTO.getMinReservas(), result.getMinReservas());
        assertEquals(descuentoDTO.getMaxReservas(), result.getMaxReservas());
        assertEquals(descuentoDTO.getPorcentajeDescuento(), result.getPorcentajeDescuento());
    }

    @Test
    void updateDescuentoClienteFrecuente_WhenNotExists_ShouldThrowException() {
        when(descuentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                descuentoService.updateDescuentoClienteFrecuente(1L, descuentoDTO));
    }

    @Test
    void deleteDescuentoClienteFrecuente_WhenExists_ShouldReturnTrue() {
        when(descuentoRepository.findById(1L)).thenReturn(Optional.of(descuento));
        doNothing().when(descuentoRepository).deleteById(1L);

        Boolean result = descuentoService.deleteDescuentoClienteFrecuente(1L);

        assertTrue(result);
    }

    @Test
    void deleteDescuentoClienteFrecuente_WhenNotExists_ShouldThrowException() {
        when(descuentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                descuentoService.deleteDescuentoClienteFrecuente(1L));
    }

    @Test
    void getPorcentajeDescuentoClienteFrecuenteByCantidadVisitas_WhenInRange_ShouldReturnPorcentaje() {
        when(descuentoRepository.findByVisitasBetweenMinimoMaximo(7))
                .thenReturn(Optional.of(descuento));

        double result = descuentoService.getPorcentajeDescuentoClienteFrecuenteByCantidadVisitas(7);

        assertEquals(15.0, result);
    }

    @Test
    void getPorcentajeDescuentoClienteFrecuenteByCantidadVisitas_WhenNotInRange_ShouldReturnZero() {
        when(descuentoRepository.findByVisitasBetweenMinimoMaximo(3))
                .thenReturn(Optional.empty());

        double result = descuentoService.getPorcentajeDescuentoClienteFrecuenteByCantidadVisitas(3);

        assertEquals(0.0, result);
    }
}