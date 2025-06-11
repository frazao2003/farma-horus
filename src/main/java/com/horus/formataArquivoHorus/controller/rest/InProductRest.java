package com.horus.formataArquivoHorus.controller.rest;

import com.horus.formataArquivoHorus.model.entity.InProduct;
import com.horus.formataArquivoHorus.model.entity.StockProduct;
import com.horus.formataArquivoHorus.model.entity.dto.InProductDTO;
import com.horus.formataArquivoHorus.model.service.InProductService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/{tenant}/products")
public class InProductRest {

    @Autowired
    InProductService inProductService;

    //salvar um inProduct
    @PostMapping(value = "/post")
    public @ResponseBody ResponseEntity<Void> postInProduct(
            @Parameter(description = "DADOS DO PRODUTO",
                    required = true,
                    content = @Content(mediaType = "application/json"))
            @RequestBody InProductDTO inProductDTO
            ){

            inProductService.insertInProduct(inProductDTO);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    //chamar pelo id
    @GetMapping(value = "get/{id}")
    public @ResponseBody ResponseEntity<InProduct> getById(@PathVariable Long id, @PathVariable String tenant){

        try{
            InProduct inProduct = inProductService.getByID(id);
            return new ResponseEntity<InProduct>(inProduct, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //chamar todos
    @GetMapping(value = "/get/all")
    public @ResponseBody ResponseEntity<List<InProduct>> getAll(@PathVariable String tenant){
        try{
            List<InProduct> inProductList = inProductService.getALl();
            return new ResponseEntity<List<InProduct>>(inProductList, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
