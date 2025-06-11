package com.horus.formataArquivoHorus.model.repository;

import com.horus.formataArquivoHorus.model.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectorRepository extends JpaRepository<Sector, String> {


    Optional<Sector> findByCnes(String cnes);
}
