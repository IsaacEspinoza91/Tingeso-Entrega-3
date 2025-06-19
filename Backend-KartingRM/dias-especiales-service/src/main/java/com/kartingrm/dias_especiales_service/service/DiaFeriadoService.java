package com.kartingrm.dias_especiales_service.service;

import com.kartingrm.dias_especiales_service.entity.DiaFeriado;
import com.kartingrm.dias_especiales_service.repository.DiaFeriadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DiaFeriadoService {

    @Autowired
    private DiaFeriadoRepository diaFeriadoRepository;


    public List<DiaFeriado> getDiasFeriados() {
        return diaFeriadoRepository.findAll();
    }

    public DiaFeriado getDiaFeriadoById(Long id) {
        Optional<DiaFeriado> dia = diaFeriadoRepository.findById(id);
        if (dia.isEmpty()) throw new EntityNotFoundException("Dia Feriado con id " + id + " no encontrado");

        return dia.get();
    }

    public DiaFeriado createDiaFeriado(DiaFeriado diaFeriado) {
        return diaFeriadoRepository.save(diaFeriado);
    }

    public DiaFeriado updateDiaFeriado(Long id, DiaFeriado diaFeriado) {
        Optional<DiaFeriado> dia = diaFeriadoRepository.findById(id);
        if (dia.isEmpty()) throw new EntityNotFoundException("Dia Feriado con id " + id + " no encontrado");

        diaFeriado.setId(id);
        return diaFeriadoRepository.save(diaFeriado);
    }

    public Boolean deleteDiaFeriado(Long id) {
        Optional<DiaFeriado> dia = diaFeriadoRepository.findById(id);
        if (dia.isEmpty()) throw new EntityNotFoundException("Dia Feriado con id " + id + " no encontrado");

        diaFeriadoRepository.deleteById(id);
        return true;
    }

    public boolean esDiaFeriado(LocalDate fecha) {
        return diaFeriadoRepository.esDiaFeriado(fecha);
    }


    // Obtiene lista de dias feriados segun anio
    public List<DiaFeriado> getDiasFeriadosByAnio(Integer anio) {
        return diaFeriadoRepository.getDiasFeriadosByAnio(anio);
    }

}
