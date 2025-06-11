package com.horus.formataArquivoHorus.model.service;


import com.horus.formataArquivoHorus.model.entity.*;
import com.horus.formataArquivoHorus.model.entity.dto.EntryProductTXTDTO;
import com.horus.formataArquivoHorus.model.repository.EntryRepository;
import com.horus.formataArquivoHorus.model.repository.OutputRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EntryAndOutputService {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private OutputRepository outputRepository;

    @Autowired
    private SectorService sectorService;

    @Transactional
    public List<String>  createTxtLine(Date competence, String cnes, String tenant){

        Sector sector = sectorService.getById(cnes);
        List<Entry> entries = entryRepository.findByCreatedAtBeforeAndSector(competence, sector);

        List<OutPut> outPuts = outputRepository.findByCreatedAtBeforeAndSector(competence, sector);

        List<OutputProduct> outputProducts = new ArrayList<>();

        List<EntryProductTXTDTO> entryProducts = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

        String compString = dateFormat.format(competence);
        compString = compString.substring(0,2);


        List<String> returnList = new ArrayList<>();

        for(OutPut value: outPuts){

            outputProducts.addAll(value.getOutputProducts());
        }

        for(Entry value: entries){

            for(EntryProduct entryProduct: value.getEntryProducts() ){

                if(entryProducts.isEmpty()){
                    EntryProductTXTDTO entryProductTXT = new EntryProductTXTDTO();
                    entryProductTXT.setAmount(entryProduct.getAmount());
                    entryProductTXT.setDescription(entryProduct.getDescription());
                    entryProductTXT.setUnit(entryProduct.getUnit());
                    entryProductTXT.setCodLot(entryProduct.getCodLot());

                    entryProducts.add(entryProductTXT);
                }else{

                    for (EntryProductTXTDTO entryProduct1: entryProducts){

                        if (Objects.equals(entryProduct1.getCodLot(), entryProduct.getCodLot())){

                            entryProduct1.setAmount(entryProduct1.getAmount() + entryProduct.getAmount());
                        }else{
                            EntryProductTXTDTO entryProductTXT = new EntryProductTXTDTO();

                            entryProductTXT.setAmount(entryProduct.getAmount());
                            entryProductTXT.setDescription(entryProduct.getDescription());
                            entryProductTXT.setUnit(entryProduct.getUnit());
                            entryProductTXT.setCodLot(entryProduct.getCodLot());
                            entryProducts.add(entryProductTXT);
                        }
                    }
                }


            }
        }

        for(EntryProductTXTDTO value: entryProducts){

            for(OutputProduct outputProduct: outputProducts){

                if(Objects.equals(value.getCodLot(), outputProduct.getCodLot()) && Objects.equals(value.getDescription(), outputProduct.getDescription())
                && value.getAmount() >= outputProduct.getAmount()){

                    value.setAmount(value.getAmount() - outputProduct.getAmount());
                }
            }
        }

        for(EntryProductTXTDTO value: entryProducts){

            StringBuilder sb = new StringBuilder();
            String codLot = value.getCodLot();
            Random random = new Random();



            /* Troca as letras do codGTIN por um numero aleatorio */
            for (int d = 0; d < codLot.length(); d++){


                char c = codLot.charAt(d);
                char v = '-';

                if (Character.isLetter(c) || c == v){

                    int randomNumber = random.nextInt(10);
                    sb.append(randomNumber);

                }else {
                    sb.append(c);
                }
            }
            codLot = sb.toString();


            String line = tenant + compString + cnes;

            // Configuração do layout do arquivo Estoque farmacia
            if (codLot.length() > 14){
                line = line + codLot.substring(0,13);
            }else{
                line += codLot;
            }

            if (line.length() < 29){
                line += " ".repeat(29 - line.length());
            }

            String description = value.getDescription();

            if (description.length() > 60){
                line += description.substring(0, 58) + " ".repeat(2);
            }else {
                line += description;

                line += " ".repeat(89 - line.length());
            }

            String unit = value.getUnit();

            if (unit.length() > 10){
                line += unit.substring(0,9);
            }else {
                line += unit;
                line += " ".repeat(99 - line.length());
            }

            String amount = String.valueOf(value.getAmount());

            line += amount;
            line += " ".repeat( 114 - line.length());

            returnList.add(line);


        }

        return  returnList;
    }
}
