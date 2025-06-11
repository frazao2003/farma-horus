package com.horus.formataArquivoHorus.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "output_product_tb")
@Data
public class OutputProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "output_id", referencedColumnName = "id")
    private OutPut output;

    @Column(name = "codLot")
    private String codLot;

    @Column(name = "description")
    private String description;

    @Column(name = "unit")
    private String unit;

    @Column(name = "amount")
    private int amount;
}

