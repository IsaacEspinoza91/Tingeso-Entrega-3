package com.kartingrm.planservice.controller;

import com.kartingrm.planservice.dto.PlanDTO;
import com.kartingrm.planservice.entity.Plan;
import com.kartingrm.planservice.service.PlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanControllerTest {

    @Mock
    private PlanService planService;

    @InjectMocks
    private PlanController planController;

    private Plan plan;
    private PlanDTO planDTO;

    @BeforeEach
    void setUp() {
        plan = new Plan();
        plan.setId(1L);
        plan.setDescripcion("Plan Premium");
        plan.setDuracionTotal(60);
        plan.setPrecioRegular(100.0);
        plan.setPrecioFinSemana(120.0);
        plan.setPrecioFeriado(150.0);

        planDTO = new PlanDTO();
        planDTO.setDescripcion("Plan Premium");
        planDTO.setDuracionTotal(60);
        planDTO.setPrecioRegular(100.0);
        planDTO.setPrecioFinSemana(120.0);
        planDTO.setPrecioFeriado(150.0);
    }

    @Test
    void getPlanes_ShouldReturnAllPlans() {
        // Arrange
        when(planService.getPlanes()).thenReturn(Arrays.asList(plan));

        // Act
        ResponseEntity<List<Plan>> response = planController.getPlanes();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(plan, response.getBody().get(0));
        verify(planService, times(1)).getPlanes();
    }

    @Test
    void getPlanById_WhenPlanExists_ShouldReturnPlan() {
        // Arrange
        when(planService.getPlanById(1L)).thenReturn(plan);

        // Act
        ResponseEntity<Plan> response = planController.getPlanById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plan, response.getBody());
        verify(planService, times(1)).getPlanById(1L);
    }

    @Test
    void getPlanById_WhenPlanNotExists_ShouldThrowException() {
        // Arrange
        when(planService.getPlanById(1L)).thenThrow(new EntityNotFoundException("Plan no encontrado"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> planController.getPlanById(1L));
        verify(planService, times(1)).getPlanById(1L);
    }

    @Test
    void createPlan_WithValidPlanDTO_ShouldReturnCreatedPlan() {
        // Arrange
        when(planService.createPlan(any(PlanDTO.class))).thenReturn(plan);

        // Act
        ResponseEntity<Plan> response = planController.createPlan(planDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plan, response.getBody());
        verify(planService, times(1)).createPlan(any(PlanDTO.class));
    }

    @Test
    void updatePlan_WhenPlanExists_ShouldReturnUpdatedPlan() {
        // Arrange
        when(planService.updatePlan(eq(1L), any(PlanDTO.class))).thenReturn(plan);

        // Act
        ResponseEntity<Plan> response = planController.updatePlan(1L, planDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plan, response.getBody());
        verify(planService, times(1)).updatePlan(eq(1L), any(PlanDTO.class));
    }

    @Test
    void updatePlan_WhenPlanNotExists_ShouldThrowException() {
        // Arrange
        when(planService.updatePlan(eq(1L), any(PlanDTO.class)))
                .thenThrow(new EntityNotFoundException("Plan no encontrado"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> planController.updatePlan(1L, planDTO));
        verify(planService, times(1)).updatePlan(eq(1L), any(PlanDTO.class));
    }

    @Test
    void deletePlan_WhenPlanExists_ShouldReturnNoContent() {
        // Arrange
        when(planService.deletePlan(1L)).thenReturn(true); // Cambiar doNothing() por when().thenReturn()

        // Act
        ResponseEntity<Boolean> response = planController.deletePlan(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(planService, times(1)).deletePlan(1L);
    }


    @Test
    void deletePlan_WhenPlanNotExists_ShouldThrowException() {
        // Arrange
        doThrow(new EntityNotFoundException("Plan no encontrado")).when(planService).deletePlan(1L);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> planController.deletePlan(1L));
        verify(planService, times(1)).deletePlan(1L);
    }
}