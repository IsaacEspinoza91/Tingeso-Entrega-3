package com.kartingrm.reportes_service.modelbase;


import java.util.Map;

public abstract class ReporteBase {

    protected Map<String, Double> ingresosPorMes;
    protected Double total;
    protected boolean esTotalGeneral;


    public Map<String, Double> getIngresosPorMes() {
        return ingresosPorMes;
    }

    public void setIngresosPorMes(Map<String, Double> ingresosPorMes) {
        this.ingresosPorMes = ingresosPorMes;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public boolean isEsTotalGeneral() {
        return esTotalGeneral;
    }

    public void setEsTotalGeneral(boolean esTotalGeneral) {
        this.esTotalGeneral = esTotalGeneral;
    }
}
