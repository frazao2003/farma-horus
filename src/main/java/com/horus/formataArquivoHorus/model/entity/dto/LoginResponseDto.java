package com.horus.formataArquivoHorus.model.entity.dto;

import com.horus.formataArquivoHorus.model.entity.Sector;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponseDto {

    String token;

    List<String> sectorID;
}
