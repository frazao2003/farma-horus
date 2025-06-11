package com.horus.formataArquivoHorus.model.entity.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
public class EntryRequestDTO {

    private List<StockProductDTO> stockProductDTOList;
    private Date entryDate;
}
