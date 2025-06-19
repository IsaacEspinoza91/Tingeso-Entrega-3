package com.kartingrm.diasespecialesservice.controller;

import com.kartingrm.diasespecialesservice.dto.DiaFeriadoDTO;
import com.kartingrm.diasespecialesservice.entity.DiaFeriado;
import com.kartingrm.diasespecialesservice.service.DiaFeriadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiaFeriadoControllerTest {

    @Mock
    private DiaFeriadoService diaFeriadoService;

    @InjectMocks
    private DiaFeriadoController diaFeriadoController;

    private DiaFeriado diaFeriado;
    private DiaFeriadoDTO diaFeriadoDTO;

    @BeforeEach
    void setUp() {
        diaFeriado = new DiaFeriado();
        diaFeriado.setId(1L);
        diaFeriado.setNombre("Navidad");
        diaFeriado.setFecha(LocalDate.of(2023, 12, 25));

        diaFeriadoDTO = new DiaFeriadoDTO();
        diaFeriadoDTO.setNombre("Navidad");
        diaFeriadoDTO.setFecha(LocalDate.of(2023, 12, 25));
    }

    @Test
    void getDiasFeriados_ShouldReturnAllDiasFeriados() {
        when(diaFeriadoService.getDiasFeriados()).thenReturn(Arrays.asList(diaFeriado));

        ResponseEntity<List<DiaFeriado>> response = diaFeriadoController.getDiasFeriados();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(diaFeriado, response.getBody().get(0));
    }

    @Test
    void getDiasFeriadoById_ShouldReturnDiaFeriado() {
        when(diaFeriadoService.getDiaFeriadoById(1L)).thenReturn(diaFeriado);

        ResponseEntity<DiaFeriado> response = diaFeriadoController.getDiasFeriadoById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(diaFeriado, response.getBody());
    }

    @Test
    void createDiaFeriado_ShouldReturnCreatedDiaFeriado() {
        when(diaFeriadoService.createDiaFeriado(any(DiaFeriadoDTO.class))).thenReturn(diaFeriado);

        ResponseEntity<DiaFeriado> response = diaFeriadoController.createDiaFeriado(diaFeriadoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(diaFeriado, response.getBody());
    }

    @Test
    void updateDiaFeriado_ShouldReturnUpdatedDiaFeriado() {
        when(diaFeriadoService.updateDiaFeriado(anyLong(), any(DiaFeriadoDTO.class))).thenReturn(diaFeriado);

        ResponseEntity<DiaFeriado> response = diaFeriadoController.updateDiaFeriado(1L, diaFeriadoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(diaFeriado, response.getBody());
    }

    @Test
    void deleteDiaFeriado_ShouldReturnNoContent() {
        when(diaFeriadoService.deleteDiaFeriado(1L)).thenReturn(true);

        ResponseEntity<Boolean> response = diaFeriadoController.deleteDiaFeriado(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(diaFeriadoService, times(1)).deleteDiaFeriado(1L);
    }
    @Test
    void esFeriado_WhenDateIsHoliday_ShouldReturnTrue() {
        when(diaFeriadoService.esDiaFeriado(any(LocalDate.class))).thenReturn(true);
        LocalDate fecha = LocalDate.of(2023, 12, 25);

        ResponseEntity<Boolean> response = diaFeriadoController.esFeriado(fecha);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void getDiasFeriadosByAnio_ShouldReturnDiasFeriados() {
        when(diaFeriadoService.getDiasFeriadosByAnio(anyInt())).thenReturn(Arrays.asList(diaFeriado));

        ResponseEntity<List<DiaFeriado>> response = diaFeriadoController.getDiasFeriadosByAnio(2023);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(diaFeriado, response.getBody().get(0));
    }
}