package com.horus.formataArquivoHorus.model.service;

import com.horus.formataArquivoHorus.model.entity.EntityPB;
import com.horus.formataArquivoHorus.model.entity.Sector;
import com.horus.formataArquivoHorus.model.entity.StockProduct;
import com.horus.formataArquivoHorus.model.entity.dto.*;
import com.horus.formataArquivoHorus.model.repository.EntityPbRepository;
import com.horus.formataArquivoHorus.model.repository.SectorRepository;
import com.horus.formataArquivoHorus.utils.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SectorService {

    @Autowired
    private Utils utils;

    @Autowired
    private EntityPbService entityPbService;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityPbRepository entityPbRepository;

    @Autowired
    private EntityManager entityManager;

    //crio um novo setor e o vinculo a uma entidade
    public void addSector(SectorDTO sectorDTO, String ecode){

        this.validNewSector(sectorDTO);

        //chamo a entidade e vinculo o setor a ela
        EntityPB entityPB = entityPbService.getEntityPb(ecode);

        Sector sector = new Sector();
        modelMapper.map(sectorDTO, sector);

        sector.setEntityPB(entityPB);

        entityPB.getSectors().add(sector);

        sectorRepository.save(sector);
        entityPbRepository.save(entityPB);

    }

    //removo um setor
    public void removeSector(String cnes){


        if(!utils.containsOnlyNumbers(cnes) || cnes.length() > 6){
            throw new IllegalArgumentException("INVALID CNES");
        }

        Optional<Sector> sector = sectorRepository.findById(cnes);

        sector.ifPresent(value -> sectorRepository.delete(value));
    }

    public List<SectorResponseDTO> getAllSectors(String ecode) {
        EntityPB entityPB = entityPbService.getEntityPb(ecode);

        return entityPB.getSectors()
                .stream()
                .map(sector -> modelMapper.map(sector, SectorResponseDTO.class))
                .toList();
    }


    @Transactional
    public SectorResponseDTO getByIdSectorStock(String id) {

        Optional<Sector> sector = sectorRepository.findById(id);

        if(sector.isEmpty()){
            throw new EntityNotFoundException("Sector dont find with this ID");
        }


        return modelMapper.map(sector, SectorResponseDTO.class);
    }

    @Transactional
    public SectorResponseDTO getSectorResponseByCnes(String cnes){


        Sector sector = this.getSectorByCnes(cnes);
        return modelMapper.map(sector, SectorResponseDTO.class);

    }

    @Transactional
    public Sector getSectorByCnes(String cnes){


        Optional<Sector> sector = sectorRepository.findByCnes(cnes);

        if(sector.isEmpty()){
            throw new EntityNotFoundException("Sector not found with this ID " + cnes);
        }

        return sector.get();

    }

    @Transactional
    public Sector getById(String id){


        Optional<Sector> sector = sectorRepository.findById(id);


        if(sector.isEmpty()){
            throw new EntityNotFoundException("Sector not found with this ID " + id);
        }

        return sector.get();

    }


    //atualizo os dados do setor
    public void updateSector(SectorDTO newSector, String id){

        this.validNewSector(newSector);

        System.out.println(id);

        Optional<Sector> sectorOld = sectorRepository.findById(id);

        
        if(sectorOld.isPresent()) {
            Sector sector = sectorOld.get();
            modelMapper.map(newSector, sector);
            sectorRepository.save(sector);
        }else {
            throw new EntityNotFoundException("SECTOR NOT FOUND");
        }
        
    }

    public void validNewSector(SectorDTO sectorDTO){
        if (!utils.isValidCPF(sectorDTO.getCpfResponsible())){

            throw new IllegalArgumentException("INVALID CPF");
        }

        if(!utils.containsOnlyNumbers(sectorDTO.getCnes()) || sectorDTO.getCnes().length() > 90){

            throw new IllegalArgumentException("INVALID CNES");
        }

        if(!utils.containsOnlyNumbers(sectorDTO.getCrf()) || sectorDTO.getCrf().length() > 5){

            throw new IllegalArgumentException("INVALID CRF");
        }

        if (!utils.isValidEstado(sectorDTO.getEstado())){
            throw new IllegalArgumentException("INVALID ESTADO");
        }
    }
}
