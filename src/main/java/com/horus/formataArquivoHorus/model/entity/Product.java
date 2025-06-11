package com.horus.formataArquivoHorus.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Table(name = "product_table")
@jakarta.persistence.Entity
@EqualsAndHashCode
public class Product {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "lotCode")
    private String lotCode;

    @Column(name = "validationDate")
    private String validationDate;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "unit")
    private String unit;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "cod_Gtin")
    private String codGtin;

    @ManyToOne
    @JoinColumn(name = "competence_horus_id")
    @JsonIgnore
    private CompetenceHorus competenceHorus;



}
