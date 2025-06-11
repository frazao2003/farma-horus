package com.horus.formataArquivoHorus.model.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StockProductResponseDTO {
    private String codLot;
    private Date expiationDate;
    private Date createdAt;
    private Integer amount;

    private InProductDTO inProduct; // resumido

    // Getters e Setters
}

