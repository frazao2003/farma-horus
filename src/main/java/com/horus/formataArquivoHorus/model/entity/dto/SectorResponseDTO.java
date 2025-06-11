package com.horus.formataArquivoHorus.model.entity.dto;

import lombok.Data;


@Data
public class SectorResponseDTO {

    private String id;
    private String name;
    private String cnes;
    private String nameResponsible;
    private String cpfResponsible;
    private String crf;
    private String address;
    private String numberAddress;
    private String estado;
    private String cidade;
    private String cep;
    private String phoneNumber;
}

