package com.horus.formataArquivoHorus.model.entity.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OutPutRequestDTO {

    private List<OutputDTO> outputDTOList;

    private Date outputDate;
}
