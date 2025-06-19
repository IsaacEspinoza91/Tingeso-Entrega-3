package com.kartingrm.diasespecialesservice.service;

import com.kartingrm.diasespecialesservice.dto.DiaFeriadoDTO;
import com.kartingrm.diasespecialesservice.entity.DiaFeriado;
import com.kartingrm.diasespecialesservice.repository.DiaFeriadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiaFeriadoServiceTest {

    @Mock
    private DiaFeriadoRepository diaFeriadoRepository;

    @InjectMocks
    private DiaFeriadoService diaFeriadoService;

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
        // Arrange
        when(diaFeriadoRepository.findAll()).thenReturn(Arrays.asList(diaFeriado));

        // Act
        List<DiaFeriado> result = diaFeriadoService.getDiasFeriados();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(diaFeriado, result.get(0));
        verify(diaFeriadoRepository, times(1)).findAll();
    }

    @Test
    void getDiaFeriadoById_WhenDiaExists_ShouldReturnDiaFeriado() {
        // Arrange
        when(diaFeriadoRepository.findById(1L)).thenReturn(Optional.of(diaFeriado));

        // Act
        DiaFeriado result = diaFeriadoService.getDiaFeriadoById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(diaFeriado, result);
        verify(diaFeriadoRepository, times(1)).findById(1L);
    }

    @Test
    void getDiaFeriadoById_WhenDiaNotExists_ShouldThrowException() {
        // Arrange
        when(diaFeriadoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> diaFeriadoService.getDiaFeriadoById(1L));
        verify(diaFeriadoRepository, times(1)).findById(1L);
    }

    @Test
    void createDiaFeriado_WithValidDTO_ShouldReturnCreatedDiaFeriado() {
        // Arrange
        when(diaFeriadoRepository.save(any(DiaFeriado.class))).thenReturn(diaFeriado);

        // Act
        DiaFeriado result = diaFeriadoService.createDiaFeriado(diaFeriadoDTO);

        // Assert
        assertNotNull(result);
        assertEquals(diaFeriadoDTO.getNombre(), result.getNombre());
        assertEquals(diaFeriadoDTO.getFecha(), result.getFecha());
        verify(diaFeriadoRepository, times(1)).save(any(DiaFeriado.class));
    }

    @Test
    void createDiaFeriado_WithNullDTO_ShouldThrowException() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> diaFeriadoService.createDiaFeriado(null));
        verify(diaFeriadoRepository, never()).save(any(DiaFeriado.class));
    }

    @Test
    void updateDiaFeriado_WhenDiaExists_ShouldReturnUpdatedDiaFeriado() {
        // Arrange
        when(diaFeriadoRepository.findById(1L)).thenReturn(Optional.of(diaFeriado));
        when(diaFeriadoRepository.save(any(DiaFeriado.class))).thenReturn(diaFeriado);

        // Act
        DiaFeriado result = diaFeriadoService.updateDiaFeriado(1L, diaFeriadoDTO);

        // Assert
        assertNotNull(result);
        assertEquals(diaFeriadoDTO.getNombre(), result.getNombre());
        assertEquals(diaFeriadoDTO.getFecha(), result.getFecha());
        verify(diaFeriadoRepository, times(1)).findById(1L);
        verify(diaFeriadoRepository, times(1)).save(any(DiaFeriado.class));
    }

    @Test
    void updateDiaFeriado_WhenDiaNotExists_ShouldThrowException() {
        // Arrange
        when(diaFeriadoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> diaFeriadoService.updateDiaFeriado(1L, diaFeriadoDTO));
        verify(diaFeriadoRepository, times(1)).findById(1L);
        verify(diaFeriadoRepository, never()).save(any(DiaFeriado.class));
    }

    @Test
    void deleteDiaFeriado_WhenDiaExists_ShouldReturnTrue() {
        // Arrange
        when(diaFeriadoRepository.findById(1L)).thenReturn(Optional.of(diaFeriado));
        doNothing().when(diaFeriadoRepository).deleteById(1L);

        // Act
        Boolean result = diaFeriadoService.deleteDiaFeriado(1L);

        // Assert
        assertTrue(result);
        verify(diaFeriadoRepository, times(1)).findById(1L);
        verify(diaFeriadoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDiaFeriado_WhenDiaNotExists_ShouldThrowException() {
        // Arrange
        when(diaFeriadoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> diaFeriadoService.deleteDiaFeriado(1L));
        verify(diaFeriadoRepository, times(1)).findById(1L);
        verify(diaFeriadoRepository, never()).deleteById(1L);
    }

    @Test
    void esDiaFeriado_WhenDateIsHoliday_ShouldReturnTrue() {
        // Arrange
        LocalDate fecha = LocalDate.of(2023, 12, 25);
        when(diaFeriadoRepository.esDiaFeriado(fecha)).thenReturn(true);

        // Act
        boolean result = diaFeriadoService.esDiaFeriado(fecha);

        // Assert
        assertTrue(result);
        verify(diaFeriadoRepository, times(1)).esDiaFeriado(fecha);
    }

    @Test
    void esDiaFeriado_WhenDateIsNotHoliday_ShouldReturnFalse() {
        // Arrange
        LocalDate fecha = LocalDate.of(2023, 12, 26);
        when(diaFeriadoRepository.esDiaFeriado(fecha)).thenReturn(false);

        // Act
        boolean result = diaFeriadoService.esDiaFeriado(fecha);

        // Assert
        assertFalse(result);
        verify(diaFeriadoRepository, times(1)).esDiaFeriado(fecha);
    }

    @Test
    void getDiasFeriadosByAnio_ShouldReturnDiasFeriados() {
        // Arrange
        Integer anio = 2023;
        when(diaFeriadoRepository.getDiasFeriadosByAnio(anio)).thenReturn(Arrays.asList(diaFeriado));

        // Act
        List<DiaFeriado> result = diaFeriadoService.getDiasFeriadosByAnio(anio);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(diaFeriado, result.get(0));
        verify(diaFeriadoRepository, times(1)).getDiasFeriadosByAnio(anio);
    }
}