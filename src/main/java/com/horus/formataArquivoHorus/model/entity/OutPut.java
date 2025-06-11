package com.horus.formataArquivoHorus.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "output_tb")
@Data
@EqualsAndHashCode
public class OutPut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "output", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OutputProduct> outputProducts;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "sector_output_fk")
    @JsonIgnore
    private Sector sector;
}
