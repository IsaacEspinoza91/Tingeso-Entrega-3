package com.kartingrm.cliente_desc_frecu_service.service;

import com.kartingrm.cliente_desc_frecu_service.entity.DescuentoClienteFrecuente;
import com.kartingrm.cliente_desc_frecu_service.repository.DescuentoClienteFrecuenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class DescuentoClienteFrecuenteService {

    @Autowired
    private DescuentoClienteFrecuenteRepository descuentoClienteFrecuenteRepository;

    public List<DescuentoClienteFrecuente> getDescuentosClienteFrecuente() {
        return descuentoClienteFrecuenteRepository.findAll();
    }

    public DescuentoClienteFrecuente getDescuentoClienteFrecuenteById(Long id) {
        Optional<DescuentoClienteFrecuente> descuento = descuentoClienteFrecuenteRepository.findById(id);
        if (descuento.isEmpty()) throw new EntityNotFoundException("Descuento "+ id +" no encontrado");

        return descuento.get();
    }

    public DescuentoClienteFrecuente createDescuentoClienteFrecuente(DescuentoClienteFrecuente descuento) {
        return descuentoClienteFrecuenteRepository.save(descuento);
    }

    public DescuentoClienteFrecuente updateDescuentoClienteFrecuente(Long id,DescuentoClienteFrecuente descuento) {
        Optional<DescuentoClienteFrecuente> descuentoOpt = descuentoClienteFrecuenteRepository.findById(id);
        if (descuentoOpt.isEmpty()) throw new EntityNotFoundException("Descuento "+ id +" no encontrado");

        descuento.setId(id);
        return descuentoClienteFrecuenteRepository.save(descuento);
    }

    public Boolean deleteDescuentoClienteFrecuente(Long id) {
        Optional<DescuentoClienteFrecuente> descuentoOpt = descuentoClienteFrecuenteRepository.findById(id);
        if (descuentoOpt.isEmpty()) throw new EntityNotFoundException("Descuento " + id + " no encontrado");

        descuentoClienteFrecuenteRepository.deleteById(id);
        return true;
    }


    // Obtener el porcentaje de descuento segun la cantidad de visitas, uso de query SQL
    public double getPorcentajeDescuentoClienteFrecuenteByCantidadVisitas(int visitas) {
        Optional<DescuentoClienteFrecuente> descuento = descuentoClienteFrecuenteRepository.findByVisitasBetweenMinimoMaximo(visitas);
        return descuento.map(DescuentoClienteFrecuente::getPorcentajeDescuento).orElse(0.0);
    }

}
