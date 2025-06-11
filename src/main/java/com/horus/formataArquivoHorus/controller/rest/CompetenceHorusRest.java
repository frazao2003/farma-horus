package com.horus.formataArquivoHorus.controller.rest;


import com.horus.formataArquivoHorus.model.entity.Product;
import com.horus.formataArquivoHorus.model.service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/{tenant}/horus")
@SecurityRequirement(name = "bearerAuth") // Aplica a todos os endpoints deste controlador
public class CompetenceHorusRest {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/import-horus", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public @ResponseBody ResponseEntity<List<Product>> importHorus(
            @Parameter(description = "Arquivo a ser importado", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    @RequestParam("file")
            MultipartFile file
            ,
            @Parameter(description = "Competência associada ao arquivo", required = true)
            @RequestParam("competence")
            String competence,
            @PathVariable String tenant,
            @RequestParam String cnes) throws IOException {
        try {
            List<Product> listProduct= productService.readHorus(file, competence, cnes, tenant);
            return new ResponseEntity<>(listProduct,HttpStatusCode.valueOf(200));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(504));
        }

    }

}
