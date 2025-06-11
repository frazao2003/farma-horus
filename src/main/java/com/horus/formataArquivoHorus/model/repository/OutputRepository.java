package com.horus.formataArquivoHorus.model.repository;

import com.horus.formataArquivoHorus.model.entity.Entry;
import com.horus.formataArquivoHorus.model.entity.OutPut;
import com.horus.formataArquivoHorus.model.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OutputRepository extends JpaRepository<OutPut, Long> {

    List<OutPut> findByCreatedAtBeforeAndSector(Date createdAt, Sector sector);

}
