package com.horus.formataArquivoHorus.model.repository;


import com.horus.formataArquivoHorus.model.entity.Entry;
import com.horus.formataArquivoHorus.model.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    List<Entry> findByCreatedAtBeforeAndSector(Date createdAt, Sector sector);
}
