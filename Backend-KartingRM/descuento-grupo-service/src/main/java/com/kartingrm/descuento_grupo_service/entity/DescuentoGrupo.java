package com.kartingrm.descuento_grupo_service.entity;

import com.kartingrm.descuento_grupo_service.modelbase.DescuentoGrupoBase;

import javax.persistence.*;

@Entity
@Table(name = "descuento_grupo", indexes = {
        @Index(name = "idx_min_max_personas", columnList = "min_personas, max_personas")
})
public class DescuentoGrupo extends DescuentoGrupoBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Constructores
    public DescuentoGrupo() {
    }

    public DescuentoGrupo(Long id, int minPersonas, int maxPersonas, double porcentajeDescuento) {
        this.id = id;
        this.minPersonas = minPersonas;
        this.maxPersonas = maxPersonas;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public DescuentoGrupo(int minPersonas, int maxPersonas, double porcentajeDescuento) {
        this.minPersonas = minPersonas;
        this.maxPersonas = maxPersonas;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
