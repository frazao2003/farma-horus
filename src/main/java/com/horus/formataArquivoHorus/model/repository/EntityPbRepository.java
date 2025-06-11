package com.horus.formataArquivoHorus.model.repository;

import com.horus.formataArquivoHorus.model.entity.EntityPB;
import com.horus.formataArquivoHorus.utils.Utils;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityPbRepository extends JpaRepository<EntityPB, String> {


    @Modifying
    @Transactional
    default void createSchema(EntityManager entityManager, String schemaName) {
        entityManager.createNativeQuery("CREATE SCHEMA IF NOT EXISTS \"" + schemaName + "\"").executeUpdate();
    }


    @Modifying
    @Transactional
    @Query(value = "ALTER SCHEMA :oldname RENAME TO :newname", nativeQuery = true)
    void renameSchema(@Param("oldname") String oldName, @Param("newname") String newName);

    @Query(value = "SELECT schemaname FROM pg_catalog.pg_schemas WHERE schemaname NOT IN ('pg_catalog', 'information_schema', 'public', 'template0', 'template1')", nativeQuery = true)
     List<String> getAllSchemaNames();

    Optional<EntityPB> findByEcode(String s);
}
