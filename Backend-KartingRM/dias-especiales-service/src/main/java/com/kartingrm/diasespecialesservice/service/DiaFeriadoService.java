package com.kartingrm.diasespecialesservice.service;

import com.kartingrm.diasespecialesservice.dto.DiaFeriadoDTO;
import com.kartingrm.diasespecialesservice.entity.DiaFeriado;
import com.kartingrm.diasespecialesservice.repository.DiaFeriadoRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DiaFeriadoService {

    private DiaFeriadoRepository diaFeriadoRepository;
    public DiaFeriadoService(DiaFeriadoRepository diaFeriadoRepository) {
        this.diaFeriadoRepository = diaFeriadoRepository;
    }

    public List<DiaFeriado> getDiasFeriados() {
        return diaFeriadoRepository.findAll();
    }

    public DiaFeriado getDiaFeriadoById(Long id) {
        Optional<DiaFeriado> dia = diaFeriadoRepository.findById(id);
        if (dia.isEmpty()) throw new EntityNotFoundException("Dia Feriado con id " + id + " no encontrado");

        return dia.get();
    }

    public DiaFeriado createDiaFeriado(DiaFeriadoDTO diaFeriadoDTO) {
        if (diaFeriadoDTO == null) throw new EntityNotFoundException("Dia Feriado no puede ser nulo");

        DiaFeriado diaFeriado = new DiaFeriado();
        diaFeriado.setNombre(diaFeriadoDTO.getNombre());
        diaFeriado.setFecha(diaFeriadoDTO.getFecha());

        return diaFeriadoRepository.save(diaFeriado);
    }

    public DiaFeriado updateDiaFeriado(Long id, DiaFeriadoDTO diaFeriadoDTO) {
        DiaFeriado diaFeriadoExistente = diaFeriadoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Dia feriado no encontrado"));

        diaFeriadoExistente.setNombre(diaFeriadoDTO.getNombre());
        diaFeriadoExistente.setFecha(diaFeriadoDTO.getFecha());

        return diaFeriadoRepository.save(diaFeriadoExistente);
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
