package com.horus.formataArquivoHorus.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Entity
@Table(name = "stock_tb")
@Data
@EqualsAndHashCode
public class StockProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false, name = "codLot_pk")
    private String codLot;

    @Column(name = "expiation_date")
    private Date expiationDate;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToOne
    @JoinColumn(name = "in_product_fk")
    private InProduct inProduct;

    @Column
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "sector_fk")
    @JsonIgnore
    private Sector sector;
}
