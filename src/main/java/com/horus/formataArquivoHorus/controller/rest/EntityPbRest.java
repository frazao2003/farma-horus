package com.horus.formataArquivoHorus.controller.rest;

import com.horus.formataArquivoHorus.model.entity.EntityPB;
import com.horus.formataArquivoHorus.model.entity.dto.EntityPbDTO;
import com.horus.formataArquivoHorus.model.service.EntityPbService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class EntityPbRest {

    @Autowired
    EntityPbService entityPbService;

    @PostMapping(value = "/post/entity")
    public @ResponseBody ResponseEntity<Void> postEntity(
            @Parameter(description = "Dados da entidade",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EntityPbDTO.class)))
            @RequestBody EntityPbDTO entityPbDTO) throws IOException {

            entityPbService.postEntityPB(entityPbDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/get/entity/{ecode}")
    public @ResponseBody ResponseEntity<EntityPB> getEntity(
            @PathVariable  String ecode) {

        System.out.println("ECODE: " + ecode);

        EntityPB entityPB = entityPbService.getEntityPb(ecode);
        return new ResponseEntity<EntityPB>(entityPB,HttpStatus.OK);

    }

    @GetMapping(value = "/get/entities")
    public @ResponseBody ResponseEntity<List<EntityPB>> getAllEntity(){

        List<EntityPB> entityPBList = entityPbService.getAll();
        return new ResponseEntity<List<EntityPB>>(entityPBList, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/entity/{ecode}")
    public @ResponseBody ResponseEntity<Void> deleteEntity(
            @PathVariable
            String ecode) {

        entityPbService.deleteEntityPB(ecode);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping(value = "/put/entity")
    public @ResponseBody ResponseEntity<Void> putEntity(
            @Parameter(description = "Dados da entidade", required = true)
            EntityPbDTO entityPbDTO,
            @Parameter(description = "COD TCE da entidade", required = true)
            String ecodeAnt) throws IOException {

        entityPbService.updateEntityPb(entityPbDTO,ecodeAnt);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
