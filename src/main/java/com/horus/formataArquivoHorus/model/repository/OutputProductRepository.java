package com.horus.formataArquivoHorus.model.repository;

import com.horus.formataArquivoHorus.model.entity.OutputProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutputProductRepository extends JpaRepository<OutputProduct, Long> {
}
