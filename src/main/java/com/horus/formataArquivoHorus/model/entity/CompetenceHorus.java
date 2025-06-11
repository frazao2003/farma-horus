package com.horus.formataArquivoHorus.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@jakarta.persistence.Entity
@Table(name = "competence_horus")
@Data
@EqualsAndHashCode
public class CompetenceHorus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "competence")
    private String competence;

    @OneToMany(mappedBy = "competenceHorus")
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "sector_fk")
    @JsonIgnore
    private Sector sector;

}
