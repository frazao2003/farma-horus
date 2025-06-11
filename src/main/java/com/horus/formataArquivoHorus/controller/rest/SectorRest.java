package com.horus.formataArquivoHorus.controller.rest;

import com.horus.formataArquivoHorus.model.entity.Sector;
import com.horus.formataArquivoHorus.model.entity.dto.SectorDTO;
import com.horus.formataArquivoHorus.model.entity.dto.SectorResponseDTO;
import com.horus.formataArquivoHorus.model.service.SectorService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/{tenant}/sector")
public class SectorRest {

    @Autowired
    SectorService sectorService;


    @PostMapping(value = "/post")
    public @ResponseBody ResponseEntity<Void> postSector(
            @Parameter(description = "Dados do Setor",
            required = true,
            content = @Content(mediaType = "application/json"))
            @RequestBody SectorDTO sector,
            @PathVariable String tenant
            ) throws IllegalStateException{
        try{
            sectorService.addSector(sector, tenant);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (IllegalStateException e){
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAll")
    public @ResponseBody ResponseEntity<List<SectorResponseDTO>> getAllSectors(
            @PathVariable String tenant
    ){
        try{
            List<SectorResponseDTO> sectorList = sectorService.getAllSectors(tenant);
            return new ResponseEntity<>(sectorList, HttpStatus.OK);

        }catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("{id}/get")
    public @ResponseBody ResponseEntity<SectorResponseDTO> getSectorById(@PathVariable String id){

        try{
            SectorResponseDTO sector = sectorService.getByIdSectorStock(id);
            return new ResponseEntity<>(sector, HttpStatus.OK);
        }catch (IllegalStateException e){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("{cnes}/get/cnes")
    public @ResponseBody ResponseEntity<SectorResponseDTO> getSectorByCnes(@PathVariable String cnes){

        try{
            SectorResponseDTO sector = sectorService.getSectorResponseByCnes(cnes);
            return new ResponseEntity<>(sector, HttpStatus.OK);
        }catch (IllegalStateException e){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }

    @DeleteMapping("/delete/{id}")
    public @ResponseBody ResponseEntity<Void> removeSector(
            @PathVariable String id, @PathVariable String tenant
    ){
        try{
            sectorService.removeSector(id);
            return new ResponseEntity<>(HttpStatus.OK);

        }catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("{id}/put")
    public @ResponseBody ResponseEntity<Void> updateSector(
            @RequestBody @Parameter(description = "Novos dados da entidade", required = true,
                    content = @Content(mediaType = "application/json") )
                    SectorDTO newSector,
            @PathVariable
            String id
    ){
        try{
            sectorService.updateSector(newSector, id);
            return new ResponseEntity<>(HttpStatus.OK);

        }catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
