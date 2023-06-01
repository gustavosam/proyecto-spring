package com.sam.springboot.apirest.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private String observacion;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties({"facturas","hibernateLazyInitializer", "handler"})
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    //@JsonBackReference
    private Cliente cliente;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<ItemFactura> items = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        this.createAt = new Date();
    }

    public Double getTotal(){
        Double total = 0.0;
        for (ItemFactura item: items){
            total = total + item.getImporte();
        }
        return total;
    }

    //CUANDO SE USA MANY_TO_ONE EN LA CLASE "A", LA CLASE "A" SERÁ EL DUEÑO DE LA RELACIÓN Y AHÍ SE UBICARÁ LA LLAVE_FORANEA

    private static final long serialVersionUID = 1L;
}
