package com.horus.formataArquivoHorus.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "entry_product_tb")
@Data
public class EntryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "entry_id", referencedColumnName = "id")
    private Entry entry;

    @Column(name = "codLot")
    private String codLot;

    @Column(name = "description")
    private String description;

    @Column(name = "unit")
    private String unit;

    @Column(name = "amount")
    private int amount;
}

