package com.kartingrm.reservas_comprobantes_service.repository;

import com.kartingrm.reservas_comprobantes_service.entity.DetalleComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleComprobanteRepository  extends JpaRepository<DetalleComprobante, Long> {

    List<DetalleComprobante> findDetalleComprobantesByIdComprobante(Long idComprobante);
}
