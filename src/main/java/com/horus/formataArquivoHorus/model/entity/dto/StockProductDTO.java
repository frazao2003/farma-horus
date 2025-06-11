package com.horus.formataArquivoHorus.model.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class StockProductDTO {


    private String codLot;

    private Date expiationDate;

    private Long idProduct;

    private Integer amount;
}
