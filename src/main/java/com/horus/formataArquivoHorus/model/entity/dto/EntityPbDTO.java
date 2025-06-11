package com.horus.formataArquivoHorus.model.entity.dto;


import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EntityPbDTO {

    private String ecode;

    private String name;

    private String address;

    private String numberAddress;

    private String estado;

    private String cidade;

    private String bairro;

    private String cep;

    private String phoneNumber;

    private String cnpj;

    private String email;

}
