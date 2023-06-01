package com.sam.springboot.apirest.models.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty
    @Size(min = 4)
    private String nombre;

    @Column(nullable = false)
    @NotEmpty
    @Size(min = 2)
    private String apellido;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email
    private String email;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "fecha_nacimiento")
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    private String foto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Region region;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"cliente", "hibernateLazyInitializer", "handler"})
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Factura> facturas = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        createAt = new Date();
    }

    private static final long serialVersionUID=1L;
}
