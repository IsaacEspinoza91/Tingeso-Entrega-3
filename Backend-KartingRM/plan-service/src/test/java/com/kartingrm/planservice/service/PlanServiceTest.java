package com.kartingrm.planservice.service;

import com.kartingrm.planservice.dto.PlanDTO;
import com.kartingrm.planservice.entity.Plan;
import com.kartingrm.planservice.repository.PlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanService planService;

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
        when(planRepository.findAll()).thenReturn(Arrays.asList(plan));

        // Act
        List<Plan> result = planService.getPlanes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(plan, result.get(0));
        verify(planRepository, times(1)).findAll();
    }

    @Test
    void getPlanById_WhenPlanExists_ShouldReturnPlan() {
        // Arrange
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        // Act
        Plan result = planService.getPlanById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(plan, result);
        verify(planRepository, times(1)).findById(1L);
    }

    @Test
    void getPlanById_WhenPlanNotExists_ShouldThrowException() {
        // Arrange
        when(planRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> planService.getPlanById(1L));
        verify(planRepository, times(1)).findById(1L);
    }

    @Test
    void createPlan_WithValidPlanDTO_ShouldReturnCreatedPlan() {
        // Arrange
        when(planRepository.save(any(Plan.class))).thenReturn(plan);

        // Act
        Plan result = planService.createPlan(planDTO);

        // Assert
        assertNotNull(result);
        assertEquals(plan.getDescripcion(), result.getDescripcion());
        assertEquals(plan.getDuracionTotal(), result.getDuracionTotal());
        assertEquals(plan.getPrecioRegular(), result.getPrecioRegular());
        assertEquals(plan.getPrecioFinSemana(), result.getPrecioFinSemana());
        assertEquals(plan.getPrecioFeriado(), result.getPrecioFeriado());
        verify(planRepository, times(1)).save(any(Plan.class));
    }

    @Test
    void createPlan_WithNullPlanDTO_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> planService.createPlan(null));
        verify(planRepository, never()).save(any(Plan.class));
    }

    @Test
    void updatePlan_WhenPlanExists_ShouldReturnUpdatedPlan() {
        // Arrange
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(planRepository.save(any(Plan.class))).thenReturn(plan);

        // Act
        Plan result = planService.updatePlan(1L, planDTO);

        // Assert
        assertNotNull(result);
        assertEquals(planDTO.getDescripcion(), result.getDescripcion());
        assertEquals(planDTO.getDuracionTotal(), result.getDuracionTotal());
        assertEquals(planDTO.getPrecioRegular(), result.getPrecioRegular());
        assertEquals(planDTO.getPrecioFinSemana(), result.getPrecioFinSemana());
        assertEquals(planDTO.getPrecioFeriado(), result.getPrecioFeriado());
        verify(planRepository, times(1)).findById(1L);
        verify(planRepository, times(1)).save(any(Plan.class));
    }

    @Test
    void updatePlan_WhenPlanNotExists_ShouldThrowException() {
        // Arrange
        when(planRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> planService.updatePlan(1L, planDTO));
        verify(planRepository, times(1)).findById(1L);
        verify(planRepository, never()).save(any(Plan.class));
    }

    @Test
    void deletePlan_WhenPlanExists_ShouldReturnTrue() {
        // Arrange
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        doNothing().when(planRepository).deleteById(1L);

        // Act
        Boolean result = planService.deletePlan(1L);

        // Assert
        assertTrue(result);
        verify(planRepository, times(1)).findById(1L);
        verify(planRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePlan_WhenPlanNotExists_ShouldThrowException() {
        // Arrange
        when(planRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> planService.deletePlan(1L));
        verify(planRepository, times(1)).findById(1L);
        verify(planRepository, never()).deleteById(1L);
    }
}