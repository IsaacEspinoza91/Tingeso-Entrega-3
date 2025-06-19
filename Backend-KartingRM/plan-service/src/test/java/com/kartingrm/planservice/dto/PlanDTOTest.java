package com.kartingrm.planservice.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlanDTOTest {

    @Test
    void testEmptyConstructor() {
        // Arrange & Act
        PlanDTO planDTO = new PlanDTO();

        // Assert
        assertNotNull(planDTO);
        assertNull(planDTO.getDescripcion());
        assertEquals(0, planDTO.getDuracionTotal());
        assertNull(planDTO.getPrecioRegular());
        assertNull(planDTO.getPrecioFinSemana());
        assertNull(planDTO.getPrecioFeriado());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String descripcion = "10 vueltas o máx 10 min";
        int duracionTotal = 60;
        Double precioRegular = 100.0;
        Double precioFinSemana = 120.0;
        Double precioFeriado = 150.0;

        // Act
        PlanDTO planDTO = new PlanDTO(
                descripcion,
                duracionTotal,
                precioRegular,
                precioFinSemana,
                precioFeriado
        );

        // Assert
        assertNotNull(planDTO);
        assertEquals(descripcion, planDTO.getDescripcion());
        assertEquals(duracionTotal, planDTO.getDuracionTotal());
        assertEquals(precioRegular, planDTO.getPrecioRegular());
        assertEquals(precioFinSemana, planDTO.getPrecioFinSemana());
        assertEquals(precioFeriado, planDTO.getPrecioFeriado());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        PlanDTO planDTO = new PlanDTO();
        String descripcion = "20 vueltas o máx 20 min";
        int duracionTotal = 30;
        Double precioRegular = 50.0;
        Double precioFinSemana = 70.0;
        Double precioFeriado = 90.0;

        // Act
        planDTO.setDescripcion(descripcion);
        planDTO.setDuracionTotal(duracionTotal);
        planDTO.setPrecioRegular(precioRegular);
        planDTO.setPrecioFinSemana(precioFinSemana);
        planDTO.setPrecioFeriado(precioFeriado);

        // Assert
        assertEquals(descripcion, planDTO.getDescripcion());
        assertEquals(duracionTotal, planDTO.getDuracionTotal());
        assertEquals(precioRegular, planDTO.getPrecioRegular());
        assertEquals(precioFinSemana, planDTO.getPrecioFinSemana());
        assertEquals(precioFeriado, planDTO.getPrecioFeriado());
    }
}