package com.horus.formataArquivoHorus.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Table(name = "ecodes")
@Entity
@Data
@EqualsAndHashCode
public class Ecodes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ecode;


}
