package com.horus.formataArquivoHorus.model.repository;

import com.horus.formataArquivoHorus.model.entity.EntryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryProductsRepository extends JpaRepository<EntryProduct, Long> {
}
