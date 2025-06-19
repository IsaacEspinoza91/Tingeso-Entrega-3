package com.kartingrm.descuento_grupo_service.controller;

import com.kartingrm.descuento_grupo_service.dto.DescuentoGrupoDTO;
import com.kartingrm.descuento_grupo_service.entity.DescuentoGrupo;
import com.kartingrm.descuento_grupo_service.service.DescuentoGrupoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DescuentoGrupoControllerTest {

    @Mock
    private DescuentoGrupoService descuentoGrupoService;

    @InjectMocks
    private DescuentoGrupoController descuentoGrupoController;

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
        when(descuentoGrupoService.getDescuentosGrupos()).thenReturn(List.of(descuentoGrupo));

        ResponseEntity<List<DescuentoGrupo>> response = descuentoGrupoController.getDescuentosGrupos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(descuentoGrupo, response.getBody().get(0));
    }

    @Test
    void getDescuentoGrupoById_ShouldReturnDescuento() {
        when(descuentoGrupoService.getDescuentoGrupoById(1L)).thenReturn(descuentoGrupo);

        ResponseEntity<DescuentoGrupo> response = descuentoGrupoController.getDescuentoGrupoById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(descuentoGrupo, response.getBody());
    }

    @Test
    void createDescuentoGrupo_ShouldReturnCreatedDescuento() {
        when(descuentoGrupoService.createDescuentoGrupo(any(DescuentoGrupoDTO.class))).thenReturn(descuentoGrupo);

        ResponseEntity<DescuentoGrupo> response = descuentoGrupoController.createDescuentoGrupo(descuentoGrupoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(descuentoGrupo, response.getBody());
    }

    @Test
    void updateDescuentoGrupo_ShouldReturnUpdatedDescuento() {
        when(descuentoGrupoService.updateDescuentoGrupo(anyLong(), any(DescuentoGrupoDTO.class))).thenReturn(descuentoGrupo);

        ResponseEntity<DescuentoGrupo> response = descuentoGrupoController.updateDescuentoGrupo(1L, descuentoGrupoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(descuentoGrupo, response.getBody());
    }

    @Test
    void deleteDescuentoGrupo_ShouldReturnNoContent() {
        when(descuentoGrupoService.deleteDescuentoGrupo(1L)).thenReturn(true);

        ResponseEntity<Boolean> response = descuentoGrupoController.deleteDescuentoGrupo(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getPorcentajeDescuentoGrupoByCantidadIntegrantes_ShouldReturnPorcentaje() {
        when(descuentoGrupoService.getPorcentajeDescuentoGrupoByCantidadIntegrantes(7)).thenReturn(15.0);

        ResponseEntity<Double> response = descuentoGrupoController.getPorcentajeDescuentoGrupoByCantidadIntegrantes(7);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(15.0, response.getBody());
    }
}