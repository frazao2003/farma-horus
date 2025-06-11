package com.horus.formataArquivoHorus.controller.rest;


import com.horus.formataArquivoHorus.model.entity.StockProduct;
import com.horus.formataArquivoHorus.model.entity.dto.EntryRequestDTO;
import com.horus.formataArquivoHorus.model.entity.dto.OutPutRequestDTO;
import com.horus.formataArquivoHorus.model.service.StockProductService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/{tenant}/stock/products")
public class StockProductRest {

    @Autowired
    private StockProductService stockProductService;

    //metodo para dar entrada em uma lista de produtos, vinculada a um setor
    @PostMapping(value = "/post/{id}")
    public @ResponseBody ResponseEntity<Void> entry(@Parameter(description = "DADOS DO PRODUTO NO ESTOQUE",
            required = true,
            content = @Content(mediaType = "application/json")) @RequestBody EntryRequestDTO entryRequestDTOS,
                                                    @PathVariable String id, @PathVariable String tenant){

            stockProductService.entry(entryRequestDTOS, id, tenant);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    //metodo para dar saida em uma lista de produtos vinculados a um setor
    @PutMapping(value = "/output/{id}")
    public @ResponseBody ResponseEntity<Void> output(@Parameter(description = "DADOS DO PRODUTO NO ESTOQUE",
            required = true,
            content = @Content(mediaType = "application/json")) @RequestBody OutPutRequestDTO outputDTOS,
                                                     @PathVariable String id, @PathVariable String tenant
    ){
        System.out.println(outputDTOS);
            stockProductService.output(outputDTOS, id);
            return new ResponseEntity<>(HttpStatus.OK);

    }

    //metodo para chamar o estoque de um setor
    @GetMapping(value = "/stock/{id}")
    public @ResponseBody ResponseEntity<List<StockProduct>> getAllBySector(@PathVariable String id, @PathVariable String tenant){
        List<StockProduct> stockProductList = stockProductService.getAllBySector(id, tenant);
        return new ResponseEntity<List<StockProduct>>(stockProductList,  HttpStatus.OK);

    }
}
