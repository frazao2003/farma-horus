package com.horus.formataArquivoHorus.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "in_product_tb")
@Data
@EqualsAndHashCode
public class InProduct {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "unit")
    private String unit;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "composicao")
    private String composicao;

}
