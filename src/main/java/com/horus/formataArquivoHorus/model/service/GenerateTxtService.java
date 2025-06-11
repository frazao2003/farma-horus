package com.horus.formataArquivoHorus.model.service;


import com.horus.formataArquivoHorus.model.entity.Sector;
import com.horus.formataArquivoHorus.model.entity.dto.SectorResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GenerateTxtService {

    @Autowired
    ProductService productService;

    @Autowired
    EntryAndOutputService entryAndOutputService;

    @Autowired
    SectorService sectorService;

    public List<File> createTxt(List<String> cnesList, Date competence, String tenant) throws IOException {

        // Caminho do arquivo
        String path = "C:/formataArquivoHorus/src/main/resources/txt";

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

        String compString = dateFormat.format(competence);



        List<String> linesHorus = productService.createTxt(compString, tenant);

        System.out.println(linesHorus);

        List<String> linesIn = new ArrayList<>();

        for(String value: cnesList){

            List<String> list = entryAndOutputService.createTxtLine(competence, value, tenant);
            linesIn.addAll(list);
        }

        List<String> lines = linesIn;
        lines.addAll(linesHorus);




        if (compString.contains("/")){
            compString = compString.replace("/", "");
        }

        // Nome do arquivo
        File file1 = new File(path + "/"+ tenant + compString + "EstoqueFarmacia.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file1))){

            for(int i = 0; lines.size() > i; i++){

                writer.write(lines.get(i));

                if (i + 1 < lines.size() && !lines.get(i+1).isEmpty()) {
                    writer.newLine();
                }else{
                    writer.write(" ");
                }

            }

        }catch (IOException e){
            throw new IOException("Error to write in the file");
        }

        File file2 = new File(path+ "/"+ tenant + compString + "Farmacia.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file2))){

            for (int i = 0; cnesList.size() > i; i++){
                Sector sector = sectorService.getSectorByCnes(cnesList.get(i));

                String line = tenant + cnesList.get(i);

                line += " ".repeat(6);

                if(sector.getName().length() > 60){
                    line += sector.getName().substring(0,59);
                }else{

                    line += sector.getName();
                    line += " ".repeat(73 - line.length());
                }

                String address = sector.getAddress() + " " + sector.getNumberAddress() + " "
                        + sector.getEntityPB().getBairro() +
                        " " + sector.getCep() + " "
                        + sector.getCidade() + "-" + sector.getEstado();

                if (address.length() > 120){
                    line += address.substring(0, 119);
                }else {
                    line += address;
                    line += " ".repeat(193 - line.length());
                }

                if(sector.getNameResponsible().length() > 60){

                    line += sector.getNameResponsible().substring(0, 59);
                }else {
                    line += sector.getNameResponsible();
                    line += " ".repeat(253 - line.length());
                }

                line += sector.getCpfResponsible() + sector.getCrf();
                line += " ".repeat(274 - line.length());

                writer.write(line);

                if(i + 1 < cnesList.size() && !cnesList.get(i+1).isEmpty()){
                    writer.newLine();
                }


            }
        }catch(IOException e){
            throw new IOException("Error to write in the file");
        }

        List<File> files = new ArrayList<>();

        files.add(file1);
        files.add(file2);

        return files;
    }

}
