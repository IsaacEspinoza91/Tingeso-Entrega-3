package com.kartingrm.reservas_comprobantes_service.model;

import com.kartingrm.reservas_comprobantes_service.entity.DetalleComprobante;

import java.util.List;

public class ComprobanteConDetallesDTO {

    private Long id;
    private Double total;
    private Boolean pagado;
    private ReservaDTO reserva;
    private List<DetalleComprobanteConClienteDTO> detalles;

    // Constructores
    public ComprobanteConDetallesDTO() {}

    public ComprobanteConDetallesDTO(Long id, Double total, Boolean pagado, ReservaDTO reserva, List<DetalleComprobanteConClienteDTO> detalles) {
        this.id = id;
        this.total = total;
        this.pagado = pagado;
        this.reserva = reserva;
        this.detalles = detalles;
    }

    // getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public ReservaDTO getReserva() {
        return reserva;
    }

    public void setReserva(ReservaDTO reserva) {
        this.reserva = reserva;
    }

    public List<DetalleComprobanteConClienteDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleComprobanteConClienteDTO> detalles) {
        this.detalles = detalles;
    }
}
