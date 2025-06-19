package com.kartingrm.descuento_grupo_service.service;

import com.kartingrm.descuento_grupo_service.dto.DescuentoGrupoDTO;
import com.kartingrm.descuento_grupo_service.entity.DescuentoGrupo;
import com.kartingrm.descuento_grupo_service.repository.DescuentoGrupoRepository;
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
class DescuentoGrupoServiceTest {

    @Mock
    private DescuentoGrupoRepository descuentoGrupoRepository;

    @InjectMocks
    private DescuentoGrupoService descuentoGrupoService;

    private DescuentoGrupo descuentoGrupo;
    private DescuentoGrupoDTO descuentoGrupoDTO;

    @BeforeEach
    void setUp() {
        descuentoGrupo = new DescuentoGrupo();
        descuentoGrupo.setId(1L);
        descuentoGrupo.setMinPersonas(5);
        descuentoGrupo.setMaxPersonas(10);
        descuentoGrupo.setPorcentajeDescuento(15.0);

        descuentoGrupoDTO = new DescuentoGrupoDTO();
        descuentoGrupoDTO.setMinPersonas(5);
        descuentoGrupoDTO.setMaxPersonas(10);
        descuentoGrupoDTO.setPorcentajeDescuento(15.0);
    }

    @Test
    void getDescuentosGrupos_ShouldReturnAllDescuentos() {
        when(descuentoGrupoRepository.findAll()).thenReturn(List.of(descuentoGrupo));

        List<DescuentoGrupo> result = descuentoGrupoService.getDescuentosGrupos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(descuentoGrupo, result.get(0));
    }

    @Test
    void getDescuentoGrupoById_WhenExists_ShouldReturnDescuento() {
        when(descuentoGrupoRepository.findById(1L)).thenReturn(Optional.of(descuentoGrupo));

        DescuentoGrupo result = descuentoGrupoService.getDescuentoGrupoById(1L);

        assertEquals(descuentoGrupo, result);
    }

    @Test
    void getDescuentoGrupoById_WhenNotExists_ShouldThrowException() {
        when(descuentoGrupoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                descuentoGrupoService.getDescuentoGrupoById(1L));
    }

    @Test
    void createDescuentoGrupo_WithValidDTO_ShouldReturnCreatedDescuento() {
        when(descuentoGrupoRepository.save(any(DescuentoGrupo.class))).thenReturn(descuentoGrupo);

        DescuentoGrupo result = descuentoGrupoService.createDescuentoGrupo(descuentoGrupoDTO);

        assertNotNull(result);
        assertEquals(descuentoGrupoDTO.getMinPersonas(), result.getMinPersonas());
        assertEquals(descuentoGrupoDTO.getMaxPersonas(), result.getMaxPersonas());
        assertEquals(descuentoGrupoDTO.getPorcentajeDescuento(), result.getPorcentajeDescuento());
    }

    @Test
    void createDescuentoGrupo_WithNullDTO_ShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () ->
                descuentoGrupoService.createDescuentoGrupo(null));
    }

    @Test
    void updateDescuentoGrupo_WhenExists_ShouldReturnUpdatedDescuento() {
        when(descuentoGrupoRepository.findById(1L)).thenReturn(Optional.of(descuentoGrupo));
        when(descuentoGrupoRepository.save(any(DescuentoGrupo.class))).thenReturn(descuentoGrupo);

        DescuentoGrupo result = descuentoGrupoService.updateDescuentoGrupo(1L, descuentoGrupoDTO);

        assertEquals(descuentoGrupoDTO.getMinPersonas(), result.getMinPersonas());
        assertEquals(descuentoGrupoDTO.getMaxPersonas(), result.getMaxPersonas());
        assertEquals(descuentoGrupoDTO.getPorcentajeDescuento(), result.getPorcentajeDescuento());
    }

    @Test
    void updateDescuentoGrupo_WhenNotExists_ShouldThrowException() {
        when(descuentoGrupoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                descuentoGrupoService.updateDescuentoGrupo(1L, descuentoGrupoDTO));
    }

    @Test
    void deleteDescuentoGrupo_WhenExists_ShouldReturnTrue() {
        when(descuentoGrupoRepository.findById(1L)).thenReturn(Optional.of(descuentoGrupo));
        doNothing().when(descuentoGrupoRepository).deleteById(1L);

        Boolean result = descuentoGrupoService.deleteDescuentoGrupo(1L);

        assertTrue(result);
    }

    @Test
    void deleteDescuentoGrupo_WhenNotExists_ShouldThrowException() {
        when(descuentoGrupoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                descuentoGrupoService.deleteDescuentoGrupo(1L));
    }

    @Test
    void getPorcentajeDescuentoGrupoByCantidadIntegrantes_WhenInRange_ShouldReturnPorcentaje() {
        when(descuentoGrupoRepository.findByCantidadBetweenMinimoMaximo(7))
                .thenReturn(Optional.of(descuentoGrupo));

        double result = descuentoGrupoService.getPorcentajeDescuentoGrupoByCantidadIntegrantes(7);

        assertEquals(15.0, result);
    }

    @Test
    void getPorcentajeDescuentoGrupoByCantidadIntegrantes_WhenNotInRange_ShouldReturnZero() {
        when(descuentoGrupoRepository.findByCantidadBetweenMinimoMaximo(3))
                .thenReturn(Optional.empty());

        double result = descuentoGrupoService.getPorcentajeDescuentoGrupoByCantidadIntegrantes(3);

        assertEquals(0.0, result);
    }
}