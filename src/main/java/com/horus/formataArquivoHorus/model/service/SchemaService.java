package com.horus.formataArquivoHorus.model.service;

import com.horus.formataArquivoHorus.controller.rest.AuthenticationRest;
import com.horus.formataArquivoHorus.model.entity.*;
import com.horus.formataArquivoHorus.model.entity.dto.RegisterDTO;
import com.horus.formataArquivoHorus.model.entity.user.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.*;

@Service
public class SchemaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    @Lazy
    private AuthenticationRest authenticationRest;

    @Autowired
    private DataSource dataSource;



    @Transactional
    public void saveEntityAndCreateTables(EntityPB entity, String tenantSchema) {

        entityManager.createNativeQuery("SET search_path TO \"" + tenantSchema + "\"").executeUpdate();

        // 3. Cria as tabelas no schema do tenant, se não existirem
        createTablesIfNotExist(tenantSchema);

        entityManager.persist(entity);

        RegisterDTO registerDTO = new RegisterDTO();

        registerDTO.setLogin("suporte@gmail.com");
        registerDTO.setPassword("123456");
        registerDTO.setRole(UserRole.ADMIN);

        List<String> cnesList = new ArrayList<>();

        cnesList.add("1");

        registerDTO.setSectorIDList(cnesList);

        authenticationRest.register(registerDTO, tenantSchema);


        entityManager.createNativeQuery("SET search_path TO \"" + "999025" + "\"").executeUpdate();
    }


    private void createTablesIfNotExist(String tenantSchema) {
        // Criação da tabela 'entity' se não existir
        String createEntityTableSQL = "CREATE TABLE \"" + tenantSchema  + "\".entity (" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "code_tce VARCHAR(255) UNIQUE, " +
                "name VARCHAR(255), " +
                "address VARCHAR(255), " +
                "number_address VARCHAR(255), " +
                "estado VARCHAR(255), " +
                "cidade VARCHAR(255), " +
                "bairro VARCHAR(255), " +
                "cep VARCHAR(255), " +
                "phone_number VARCHAR(255), " +
                "cnpj VARCHAR(255), " +
                "email VARCHAR(255), " +
                "CONSTRAINT email_unique UNIQUE (email)" +
                ");";
        entityManager.createNativeQuery(createEntityTableSQL).executeUpdate();

        String createSectorTableSQL = "CREATE TABLE \"" + tenantSchema  + "\".sector_tb (" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "name VARCHAR(255), " +
                "cnes VARCHAR(255) UNIQUE, " +
                "name_responsible VARCHAR(255), " +
                "cpf_responsible VARCHAR(255), " +
                "crf VARCHAR(255), " +
                "address VARCHAR(255), " +
                "number_address VARCHAR(255), " +
                "estado VARCHAR(255), " +
                "cidade VARCHAR(255), " +
                "cep VARCHAR(255), " +
                "phone_number VARCHAR(255), " +
                "entity_pb_fk BIGINT, " +
                "FOREIGN KEY (entity_pb_fk) REFERENCES \"" + tenantSchema + "\".entity(id)" +
                ");";
        entityManager.createNativeQuery(createSectorTableSQL).executeUpdate();

        // Criação da tabela 'competence_horus' se não existir
        String createCompetenceHorusTableSQL = "CREATE TABLE \"" + tenantSchema  + "\".competence_horus (" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "competence VARCHAR(255), " +
                "sector_fk BIGINT, " +
                "FOREIGN KEY (sector_fk) REFERENCES \"" + tenantSchema  + "\".sector_tb(id)" +
                ");";
        entityManager.createNativeQuery(createCompetenceHorusTableSQL).executeUpdate();

        // Criação da tabela 'product_table' com a chave estrangeira para 'competence_horus'
        String createProductTableSQL = "CREATE TABLE \"" + tenantSchema  + "\".product_table (" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "description VARCHAR(255), " +
                "lotCode VARCHAR(255), " +
                "validationDate VARCHAR(255), " +
                "manufacturer VARCHAR(255), " +
                "unit VARCHAR(255), " +
                "amount INT, " +
                "cod_Gtin VARCHAR(255), " +
                "competence_horus_id BIGINT, " +
                "FOREIGN KEY (competence_horus_id) REFERENCES \"" + tenantSchema  + "\".competence_horus(id)" +
                ");";
        entityManager.createNativeQuery(createProductTableSQL).executeUpdate();

        // Criação da tabela 'in_product_tb' se não existir
        String createInProductTableSQL = "CREATE TABLE \"" + tenantSchema  + "\".in_product_tb (" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "composicao VARCHAR(255), " +
                "description VARCHAR(255), " +
                "manufacturer VARCHAR(255), " +
                "unit VARCHAR(255)" +
                ");";
        entityManager.createNativeQuery(createInProductTableSQL).executeUpdate();

        // Criação da tabela 'stock_tb' se não existir
        String createStockTableSQL = "CREATE TABLE \"" + tenantSchema  + "\".stock_tb (" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "codLot_pk VARCHAR(255) UNIQUE, " +
                "expiation_date TIMESTAMP, " +
                "created_at TIMESTAMP, " +
                "in_product_fk BIGINT, " + // chave estrangeira para 'InProduct'
                "amount INT, " +
                "sector_fk BIGINT, " + // chave estrangeira para 'Sector'
                "FOREIGN KEY (in_product_fk) REFERENCES \"" + tenantSchema + "\".in_product_tb(id), " +
                "FOREIGN KEY (sector_fk) REFERENCES \"" + tenantSchema + "\".sector_tb(id)" +
                ");";
        entityManager.createNativeQuery(createStockTableSQL).executeUpdate();


        // Criação da tabela 'entry_tb' para registrar as entradas
        String createEntryTableSQL = "CREATE TABLE \"" + tenantSchema + "\".entry_tb (" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "created_at TIMESTAMP, " +
                "sector_entry_fk BIGINT, " +
                "FOREIGN KEY (sector_entry_fk) REFERENCES \"" + tenantSchema + "\".sector_tb(id)" +
                ");";
        entityManager.createNativeQuery(createEntryTableSQL).executeUpdate();

        // Criação da tabela 'entry_product_tb' para armazenar os produtos associados à entrada
        String createEntryProductTableSQL = "CREATE TABLE \"" + tenantSchema + "\".entry_product_tb (" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "entry_id BIGINT, " +
                "codLot VARCHAR(255), " +
                "amount INT, " +
                "unit VARCHAR(255), " +
                "description VARCHAR(255), " +
                "FOREIGN KEY (entry_id) REFERENCES \"" + tenantSchema + "\".entry_tb(id)" +
                ");";
        entityManager.createNativeQuery(createEntryProductTableSQL).executeUpdate();

        // Criação da tabela 'output_tb' para registrar as saídas
        String createOutputTableSQL = "CREATE TABLE \"" + tenantSchema + "\".output_tb (" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "created_at TIMESTAMP, " +
                "sector_output_fk BIGINT, " +
                "FOREIGN KEY (sector_output_fk) REFERENCES \"" + tenantSchema + "\".sector_tb(id)" +
                ");";
        entityManager.createNativeQuery(createOutputTableSQL).executeUpdate();

        // Criação da tabela 'output_product_tb' para armazenar os produtos associados à saída
        String createOutputProductTableSQL = "CREATE TABLE \"" + tenantSchema + "\".output_product_tb (" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "output_id BIGINT, " +
                "codLot VARCHAR(255), " +
                "amount INT, " +
                "unit VARCHAR(255), " +
                "description VARCHAR(255), " +
                "FOREIGN KEY (output_id) REFERENCES \"" + tenantSchema + "\".output_tb(id)" +
                ");";
        entityManager.createNativeQuery(createOutputProductTableSQL).executeUpdate();

        // Criação da tabela 'users' se não existir
        String createUsersTableSQL = "CREATE TABLE \"" + tenantSchema + "\".users (" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "login VARCHAR(255) UNIQUE, " +
                "password VARCHAR(255), " +
                "role VARCHAR(50)" +
                ");";
        entityManager.createNativeQuery(createUsersTableSQL).executeUpdate();

        // Criação da tabela 'user_cnes' para armazenar os setores (cnes) de cada usuário
        String createUserCnesTableSQL = "CREATE TABLE \"" + tenantSchema + "\".user_cnes (" +
                "user_id BIGINT NOT NULL, " +
                "sector_id VARCHAR(255), " +
                "FOREIGN KEY (user_id) REFERENCES \"" + tenantSchema + "\".users(id) ON DELETE CASCADE" +
                ");";
        entityManager.createNativeQuery(createUserCnesTableSQL).executeUpdate();

    }


    public void deleteSchema(String schemaName){

        String dropSchemaSQL = "DROP SCHEMA \"" + schemaName + "\" CASCADE";
        entityManager.createNativeQuery(dropSchemaSQL).executeUpdate();
    }


}

