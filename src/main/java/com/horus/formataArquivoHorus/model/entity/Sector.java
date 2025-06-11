package com.horus.formataArquivoHorus.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horus.formataArquivoHorus.model.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Table(name = "sector_tb")
@Data
@EqualsAndHashCode
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(unique = true, nullable = false, name = "cnes")
    private String cnes;

    @Column(name = "name_responsible")
    private String nameResponsible;

    @Column(name = "cpf_responsible")
    private String cpfResponsible;

    @Column(name = "crf")
    private String crf;

    @Column(name = "address")
    private String address;

    @Column(name = "number_address")
    private String numberAddress;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "cep")
    private String cep;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "entity_pb_fk")
    @JsonIgnore
    private EntityPB entityPB;

    @OneToMany(mappedBy = "sector")
    private List<CompetenceHorus> competenceHorusList;

    @OneToMany(mappedBy = "sector")
    private List<StockProduct> stockProducts;

    @OneToMany(mappedBy = "sector")
    private List<Entry> entries;

    @OneToMany(mappedBy = "sector")
    private List<OutPut> outPuts;


}
