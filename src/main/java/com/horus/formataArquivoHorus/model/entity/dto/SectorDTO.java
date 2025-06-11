package com.horus.formataArquivoHorus.model.entity.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
public class SectorDTO {

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

