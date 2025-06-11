package com.horus.formataArquivoHorus.model.repository;

import com.horus.formataArquivoHorus.model.entity.StockProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockProductRepository extends JpaRepository<StockProduct, Long> {
}
