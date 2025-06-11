package com.horus.formataArquivoHorus.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horus.formataArquivoHorus.exception.EntityAlreadyExistsException;
import com.horus.formataArquivoHorus.model.entity.dto.EntityPbDTO;
import com.horus.formataArquivoHorus.model.repository.EcodesRepository;
import com.horus.formataArquivoHorus.utils.Utils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Table(name = "entity")
@Getter
public class EntityPB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "code_tce")
    private String ecode;

    @Column(name = "name")
    private String name;

    @Column (name = "address")
    private String address;

    @Column(name = "number_address")
    private String numberAddress;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cep")
    private String cep;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name="cnpj")
    private String cnpj;

    @Column(name = "email")
    @Email
    private String email;

    @OneToMany(mappedBy = "entityPB")
    @JsonIgnore
    private List<Sector> sectors;


    public void insertEcode(String ecode){
        // Verifica se a entidade já existe
        this.ecode = ecode;

    }

    public  void insertCNPJ(String cnpj){
        this.cnpj = cnpj;
    }

    public void insertCEP(String cep){
        this.cep = cep;
    }

    public void insertPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void insertEstado(String estado){
        this.estado = estado;
    }

    public void insertName(String name) {
        this.name = name;
    }

    public void insertAddress(String address) {
        this.address = address;
    }

    public void insertNumberAddress(String numberAddress) {
        this.numberAddress = numberAddress;
    }

    public void insertCidade(String cidade) {
        this.cidade = cidade;
    }

    public void insertBairro(String bairro) {
        this.bairro = bairro;
    }

    public void insertEmail(String email) {
        this.email = email;
    }

    public void insertSectors(List<Sector> sectors){
        this.sectors = sectors;
    }



}
