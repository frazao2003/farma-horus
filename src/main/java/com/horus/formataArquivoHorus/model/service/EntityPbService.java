package com.horus.formataArquivoHorus.model.service;

import com.horus.formataArquivoHorus.exception.EntityAlreadyExistsException;
import com.horus.formataArquivoHorus.model.entity.Ecodes;
import com.horus.formataArquivoHorus.model.entity.EntityPB;
import com.horus.formataArquivoHorus.model.entity.Sector;
import com.horus.formataArquivoHorus.model.entity.dto.EntityPbDTO;
import com.horus.formataArquivoHorus.model.entity.user.User;
import com.horus.formataArquivoHorus.model.repository.EcodesRepository;
import com.horus.formataArquivoHorus.model.repository.EntityPbRepository;
import com.horus.formataArquivoHorus.model.repository.SectorRepository;
import com.horus.formataArquivoHorus.utils.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EntityPbService {

    @Autowired
    private EntityPbRepository entityPbRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private ModelMapper modelMapper;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private EcodesRepository ecodesRepository;

    @Autowired
    private SchemaService schemaService;

    @Autowired
    SectorRepository sectorRepository;

    //metodo para criar uma nova entidade, que também criara o schema dessa entidade e as novas tabelas
    @Transactional
    public void postEntityPB(EntityPbDTO entityPbDTO) throws IOException {


        //crio a entidade nova, e passo os dados do DTO, para a entidade
        EntityPB entityPB = new EntityPB();
        this.parseDTO(entityPbDTO, entityPB);

        //crio o schema fazendo as validações necessárias, e crio a nova entidade
        try {

            if(utils.isValidSchemaName(entityPbDTO.getEcode())) {
                entityPbRepository.createSchema(entityManager, entityPB.getEcode());
            }else {
                throw new RuntimeException("Invalid Schema Name");
            }
        }catch (RuntimeException e){
            throw new RuntimeException("Error to create new schema");
        }

        schemaService.saveEntityAndCreateTables(entityPB, entityPB.getEcode());

        List<Sector> sectorList = new ArrayList<>();

        Sector sector = new Sector();

        sector.setEntityPB(entityPB);
        sector.setCnes("000000");
        sector.setCep(entityPB.getCep());
        sector.setCidade(entityPB.getCidade());
        sector.setCpfResponsible("00000000000");
        sector.setCrf("000000");
        sector.setAddress(entityPB.getAddress());
        sector.setEstado(entityPB.getEstado());
        sector.setName("ADMINISTRADOR");
        sector.setNameResponsible("RESPONSAVEL");
        sector.setPhoneNumber("87878787");

        entityManager.createNativeQuery("SET search_path TO \"" + entityPB.getEcode() + "\"").executeUpdate();

        sectorList.add(sector);

        entityPB.insertSectors(sectorList);

        sectorRepository.save(sector);

        Ecodes ecodes = new Ecodes();

        ecodes.setEcode(entityPB.getEcode());

        entityManager.createNativeQuery("SET search_path TO \"" + "999025" + "\"").executeUpdate();

        ecodesRepository.save(ecodes);


    }

    //Metodo para deletar a entidade e seu schema
    @Transactional
    public void deleteEntityPB(String ecode) {

        if (ecode == null) {
            throw new IllegalArgumentException("ecode não pode ser nulo");
        }

        entityManager.createNativeQuery("SET search_path TO \"" + ecode + "\"").executeUpdate();

        Optional<EntityPB> opEntityPB = entityPbRepository.findById(ecode);

        //verifico se a entidade existe, e a deleto junto com seu schema
        if (opEntityPB.isPresent()) {

            EntityPB entityPB = opEntityPB.get();

            entityPbRepository.delete(entityPB);

            schemaService.deleteSchema(ecode);

            ecodesRepository.delete(ecodesRepository.findByEcode(ecode));

        } else {
            throw new RuntimeException("Entity isn't registered");
        }
    }

    //metodo para atualizar uma entidade, dependendo de quais dados vou enviar
    @Transactional
    public void updateEntityPb(EntityPbDTO entityPbDTO, String ecodeAnt) throws IOException {

        Optional<EntityPB> opEntityPB = entityPbRepository.findById(ecodeAnt);

        Ecodes ecodes = new Ecodes();

        if(opEntityPB.isPresent()){

            EntityPB entityPB = opEntityPB.get();
            this.parseDTO(entityPbDTO, entityPB);

            if(!Objects.equals(entityPB.getEcode(), ecodeAnt)){
                entityPbRepository.renameSchema(ecodeAnt, entityPB.getEcode());

                ecodes = ecodesRepository.findByEcode(ecodeAnt);
                ecodes.setEcode(entityPB.getEcode());
            }
        }else {
            throw new RuntimeException("Entity isn't registered");
        }
    }

    //metodo para chamar uma entidade pelo seu ecode
    @Transactional
    public EntityPB getEntityPb(String ecode) {

        if(ecode == null){
            throw new IllegalArgumentException("ECODE is required");
        }

        entityManager.createNativeQuery("SET search_path TO \"" + ecode + "\"").executeUpdate();

        Optional<EntityPB> opEntityPB = entityPbRepository.findByEcode(ecode);

        if (opEntityPB.isPresent()) {

            return opEntityPB.get();

        } else {
            throw new RuntimeException("Entity isn't registered");
        }
    }

    //Chama todos os entitypb em todos os schemas
    @Transactional
    public List<EntityPB> getAll(){
        List<EntityPB> entityPBList = new ArrayList<>();

        List<Ecodes> schemaNames = ecodesRepository.findAll();

        for (Ecodes value: schemaNames){
            EntityPB entityPB = this.getEntityPb(value.getEcode());
            entityPBList.add(entityPB);
        }

        entityManager.createNativeQuery("SET search_path TO \"" + "999025" + "\"").executeUpdate();


        return entityPBList;
    }

    public void validateAndInsertEcode(EntityPB entity, String ecode) {
        if (ecode == null || !utils.containsOnlyNumbers(ecode)) {
            throw new IllegalArgumentException("ECODE is invalid. It must contain only numbers.");
        }

        if (ecodesRepository.findByEcode(ecode) != null) {
            throw new EntityAlreadyExistsException("Entity with ECODE " + ecode + " is already registered");
        }

        entity.insertEcode(ecode);
    }

    public  void validateAndInsertCNPJ(String cnpj, EntityPB entityPB){
        // Verifica se o CNPJ é válido
        if (!utils.isValidCNPJ(cnpj)) {
            throw new IllegalArgumentException("Invalid CNPJ. Please provide a valid CNPJ.");
        }

        entityPB.insertCNPJ(cnpj);
    }

    public void validateAndInsertCEP(String cep, EntityPB entityPB){
        // Verifica se o CEP é válido
        if (cep.length() != 8 || !utils.containsOnlyNumbers(cep)) {
            throw new IllegalArgumentException("Invalid CEP. It must be 8 digits long and contain only numbers.");
        }

        entityPB.insertCEP(cep);
    }

    public void validateAndInsertPhoneNumber(String phoneNumber, EntityPB entityPB){
        // Verifica se o número de telefone é válido
        if (!utils.containsOnlyNumbers(phoneNumber) || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Invalid Phone Number. Please provide a valid phone number with only numbers.");
        }

        entityPB.insertPhoneNumber(phoneNumber);
    }

    public void validateAndInsertEstado(String estado, EntityPB entityPB){

        if(utils.isValidEstado(estado)){
            entityPB.insertEstado(estado);
        }
    }

    public void parseDTO(EntityPbDTO dto, EntityPB entityPB){

        entityPB.insertName(dto.getName());
        entityPB.insertAddress(dto.getAddress());
        entityPB.insertNumberAddress(dto.getNumberAddress());
        this.validateAndInsertEstado(dto.getEstado(), entityPB);
        entityPB.insertCidade(dto.getCidade());
        entityPB.insertBairro(dto.getBairro());
        this.validateAndInsertCEP(dto.getCep(), entityPB);
        this.validateAndInsertPhoneNumber(dto.getPhoneNumber(), entityPB);
        entityPB.insertEmail(dto.getEmail());
        this.validateAndInsertCNPJ(dto.getCnpj() , entityPB);
        this.validateAndInsertEcode(entityPB, dto.getEcode());
    }

}
