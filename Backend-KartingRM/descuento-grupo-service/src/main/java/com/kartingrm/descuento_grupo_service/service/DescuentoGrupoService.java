package com.kartingrm.descuento_grupo_service.service;

import com.kartingrm.descuento_grupo_service.dto.DescuentoGrupoDTO;
import com.kartingrm.descuento_grupo_service.entity.DescuentoGrupo;
import com.kartingrm.descuento_grupo_service.repository.DescuentoGrupoRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class DescuentoGrupoService {

    private DescuentoGrupoRepository descuentoGrupoRepository;
    public DescuentoGrupoService(DescuentoGrupoRepository descuentoGrupoRepository) {
        this.descuentoGrupoRepository = descuentoGrupoRepository;
    }


    public List<DescuentoGrupo> getDescuentosGrupos(){
        return descuentoGrupoRepository.findAll();
    }

    public DescuentoGrupo getDescuentoGrupoById(Long id){
        Optional<DescuentoGrupo> descuento = descuentoGrupoRepository.findById(id);
        if (descuento.isEmpty()) throw new EntityNotFoundException("Regla de descuento id " + id + " no encontrado");

        return descuento.get();
    }

    public DescuentoGrupo createDescuentoGrupo(DescuentoGrupoDTO descuento){
        if (descuento == null) throw new EntityNotFoundException("Descuento no encontrado");

        DescuentoGrupo descuentoGrupo = new DescuentoGrupo();
        descuentoGrupo.setMinPersonas(descuento.getMinPersonas());
        descuentoGrupo.setMaxPersonas(descuento.getMaxPersonas());
        descuentoGrupo.setPorcentajeDescuento(descuento.getPorcentajeDescuento());

        return descuentoGrupoRepository.save(descuentoGrupo);
    }

    public DescuentoGrupo updateDescuentoGrupo(Long id, DescuentoGrupoDTO descuento){
        DescuentoGrupo descuentoGrupoExistente = descuentoGrupoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Descuento no encontrado"));

        descuentoGrupoExistente.setMinPersonas(descuento.getMinPersonas());
        descuentoGrupoExistente.setMaxPersonas(descuento.getMaxPersonas());
        descuentoGrupoExistente.setPorcentajeDescuento(descuento.getPorcentajeDescuento());

        return descuentoGrupoRepository.save(descuentoGrupoExistente);
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
