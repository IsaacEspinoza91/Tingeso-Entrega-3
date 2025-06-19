package com.kartingrm.descuento_grupo_service.service;

import com.kartingrm.descuento_grupo_service.entity.DescuentoGrupo;
import com.kartingrm.descuento_grupo_service.repository.DescuentoGrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class DescuentoGrupoService {

    @Autowired
    private DescuentoGrupoRepository descuentoGrupoRepository;


    public List<DescuentoGrupo> getDescuentosGrupos(){
        return descuentoGrupoRepository.findAll();
    }

    public DescuentoGrupo getDescuentoGrupoById(Long id){
        Optional<DescuentoGrupo> descuento = descuentoGrupoRepository.findById(id);
        if (descuento.isEmpty()) throw new EntityNotFoundException("Regla de descuento id " + id + " no encontrado");

        return descuento.get();
    }

    public DescuentoGrupo createDescuentoGrupo(DescuentoGrupo descuento){
        return descuentoGrupoRepository.save(descuento);
    }

    public DescuentoGrupo updateDescuentoGrupo(Long id, DescuentoGrupo descuento){
        Optional<DescuentoGrupo> descuentoGrupoOptional = descuentoGrupoRepository.findById(id);
        if (descuentoGrupoOptional.isEmpty()) throw new EntityNotFoundException("Regla de descuento id " + id + " no encontrado");

        descuento.setId(id);
        return descuentoGrupoRepository.save(descuento);
    }

    public Boolean deleteDescuentoGrupo(Long id){
        Optional<DescuentoGrupo> descuentoGrupoOptional = descuentoGrupoRepository.findById(id);
        if (descuentoGrupoOptional.isEmpty()) throw new EntityNotFoundException("Regla de descuento id " + id + " no encontrado");

        descuentoGrupoRepository.deleteById(id);
        return true;
    }

    // Obtener el porcentaje de descuento segun la cantidad de integrantes del grupo, uso de query SQL
    public double getPorcentajeDescuentoGrupoByCantidadIntegrantes(int cantidad){
        Optional<DescuentoGrupo> descuentoGrupoObtenido = descuentoGrupoRepository.findByCantidadBetweenMinimoMaximo(cantidad);
        return descuentoGrupoObtenido.map(DescuentoGrupo::getPorcentajeDescuento).orElse(0.0);
    }

}
