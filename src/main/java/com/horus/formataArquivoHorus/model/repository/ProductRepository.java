package com.horus.formataArquivoHorus.model.repository;

import com.horus.formataArquivoHorus.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.competenceHorus.id = :competenceHorusId")
    List<Product> findByCompetenceHorusId(String competenceHorusId);
}
