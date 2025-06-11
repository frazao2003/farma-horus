package com.horus.formataArquivoHorus.controller.rest;

import com.horus.formataArquivoHorus.model.service.GenerateTxtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/{tenant}/txt")
public class GenerateTxtRest {

        @Autowired
        private GenerateTxtService generateTxtService;

        @PostMapping("/create")
        public ResponseEntity<List<File>> createTxt(
                @RequestParam("cnesList") List<String> cnesList,
                @RequestParam("competence") @DateTimeFormat(pattern = "yyyy-MM-dd") Date competence,
                @PathVariable("tenant") String tenant) {

            try {


                // Chama o método createTxt do GenerateTxtService
                List<File> files = generateTxtService.createTxt(cnesList, competence, tenant);

                // Retorna os arquivos gerados
                return new ResponseEntity<>(files, HttpStatus.OK);

            } catch (IOException e) {
                // Se ocorrer um erro, retorna erro 500 com a mensagem de erro
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}

