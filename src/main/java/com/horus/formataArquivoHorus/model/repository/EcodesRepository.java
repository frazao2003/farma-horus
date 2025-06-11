package com.horus.formataArquivoHorus.model.repository;

import com.horus.formataArquivoHorus.model.entity.Ecodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcodesRepository extends JpaRepository<Ecodes, Long> {

    Ecodes findByEcode(String ecode);
}
