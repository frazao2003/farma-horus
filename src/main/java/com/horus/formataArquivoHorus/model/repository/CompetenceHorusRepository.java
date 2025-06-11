package com.horus.formataArquivoHorus.model.repository;

import com.horus.formataArquivoHorus.model.entity.CompetenceHorus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetenceHorusRepository extends JpaRepository<CompetenceHorus, Long> {


    CompetenceHorus findByCompetence(String compString);
}
