package com.horus.formataArquivoHorus.model.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horus.formataArquivoHorus.model.entity.Entry;
import com.horus.formataArquivoHorus.model.entity.EntryProduct;
import com.horus.formataArquivoHorus.model.entity.Sector;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EntryProductTXTDTO {

    private Entry entry;

    private String codLot;

    private String description;

    private String unit;

    private int amount;
}
