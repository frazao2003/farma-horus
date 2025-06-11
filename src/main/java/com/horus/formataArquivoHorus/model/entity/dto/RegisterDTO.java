package com.horus.formataArquivoHorus.model.entity.dto;

import com.horus.formataArquivoHorus.model.entity.user.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class RegisterDTO {

    private String login;

    private String password;

    private UserRole role;

    private List<String> sectorIDList;
}
