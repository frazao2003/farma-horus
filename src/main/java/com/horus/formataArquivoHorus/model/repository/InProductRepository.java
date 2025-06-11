package com.horus.formataArquivoHorus.model.repository;

import com.horus.formataArquivoHorus.model.entity.InProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InProductRepository extends JpaRepository<InProduct, String> {
}
