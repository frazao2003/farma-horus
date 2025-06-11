package com.horus.formataArquivoHorus.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ProductTxt {

    private String codGTIN;

    private Integer amount;

    private String description;

    private String unit;

    private String cnes;

}
