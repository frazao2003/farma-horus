package com.horus.formataArquivoHorus.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "entry_tb")
@Data
@EqualsAndHashCode
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EntryProduct> entryProducts;

    @Column(name = "created_at")
    private Date createdAt;



    @ManyToOne
    @JoinColumn(name = "sector_output_fk")
    @JsonIgnore
    private Sector sector;
}
