package com.horus.formataArquivoHorus.utils;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Utils {

    public boolean isValidCPF(String cpf) {
        if (cpf == null) return false;
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

        int[] pesos1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        return calcularDigitoVerificador(cpf, pesos1) == cpf.charAt(9) - '0' &&
                calcularDigitoVerificador(cpf, pesos2) == cpf.charAt(10) - '0';
    }

    public boolean isValidCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) return false;

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        return calcularDigitoVerificador(cnpj, pesos1) == cnpj.charAt(12) - '0' &&
                calcularDigitoVerificador(cnpj, pesos2) == cnpj.charAt(13) - '0';
    }

    private int calcularDigitoVerificador(String documento, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += (documento.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    public boolean containsOnlyNumbers(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("\\d+");
    }

    public boolean isValidSchemaName(String schemaName) {
        return schemaName != null && schemaName.matches("^[a-zA-Z0-9_]+$");
    }

    public boolean isValidEstado(String estado){
        List<String> listEstado = List.of(
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG",
                "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        );

        return listEstado.contains(estado) || !estado.isEmpty();

    }
}

