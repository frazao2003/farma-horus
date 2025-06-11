package com.horus.formataArquivoHorus.model.service;


import com.horus.formataArquivoHorus.model.entity.CompetenceHorus;
import com.horus.formataArquivoHorus.model.entity.Product;
import com.horus.formataArquivoHorus.model.entity.ProductTxt;
import com.horus.formataArquivoHorus.model.entity.Sector;
import com.horus.formataArquivoHorus.model.repository.CompetenceHorusRepository;
import com.horus.formataArquivoHorus.model.repository.ProductRepository;
import com.horus.formataArquivoHorus.model.repository.SectorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.SysexMessage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class ProductService {

    @Autowired
    private CompetenceHorusRepository competenceHorusRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private SectorRepository sectorRepository;

    @PersistenceContext
    private EntityManager entityManager;


    // Função para importar o arquivo do hórus
    public List<Product> readHorus(MultipartFile file, String competence, String cnes, String tenant) throws IOException
    {
        try (InputStream inputStream = file.getInputStream()) {

            Workbook workbook =  new HSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            List<Product> listProduct = new ArrayList<>();

            String[] productDescription = null;

            String description = null;

            String codGTIN = null;

            Sector sector = sectorService.getById(cnes);

            CompetenceHorus competenceHorus = new CompetenceHorus();
            competenceHorus.setCompetence(competence);
            competenceHorus.setSector(sector);

            // Ler todas as linhas da planilha
            int numofrows = sheet.getPhysicalNumberOfRows();

            for (int i = 4; i < numofrows - 4; i++){

                Row row = sheet.getRow(i);
                // Verifica se é uma linha de descrição
                if (row.getCell(1).getCellType() != CellType.BLANK) {
                    productDescription = row.getCell(1).getStringCellValue().split(" ", 2);

                    // Separa a descrição do codGTIN
                    if (productDescription.length > 1){

                        codGTIN = productDescription[0];

                        description = productDescription[1];
                    }

                // Verifica se é uma linha de dados do produto
                }else if (row.getCell(1).getCellType() == CellType.BLANK) {
                    String manufacturer;
                    String unitOfMeasurement;
                    String amount;
                    String lotCode;
                    String validationDate;

                    try {
                        manufacturer = row.getCell(3).getStringCellValue();

                    } catch (IllegalStateException e) {
                        throw new IllegalStateException("Erro na linha:" + (i - 1) + "Célula:" + 4);
                    }

                    try {
                        unitOfMeasurement = row.getCell(2).getStringCellValue();

                    } catch (IllegalStateException e) {
                        throw new IllegalStateException("Erro na linha:" + (i - 1) + "Célula:" + 3);
                    }

                    try {
                        amount = row.getCell(16).getStringCellValue();

                    } catch (IllegalStateException e) {
                        throw new IllegalStateException("Erro na linha:" + (i - 1) + "Célula:" + 17);
                    }
                    try {
                        lotCode = row.getCell(9).getStringCellValue();

                    } catch (NullPointerException e) {
                        throw new NullPointerException("Erro na linha:" + (i - 1) + "Célula:" + 10);
                    }
                    try {
                        validationDate = row.getCell(8).getStringCellValue();

                    } catch (NullPointerException e) {
                        throw new NullPointerException("Erro na linha:" + (i - 1) + "Célula:" + 9);
                    }


                    //Tira o . da quantidade e transform em inteiro
                    if (amount.contains(".")) {
                        amount = amount.replace(".", "");
                    }

                    Integer amount2 = Integer.parseInt(amount);

                    Product product = new Product();
                    product.setAmount(amount2);
                    product.setLotCode(lotCode);
                    product.setManufacturer(manufacturer);
                    product.setValidationDate(validationDate);
                    product.setDescription(description);
                    product.setUnit(unitOfMeasurement);
                    product.setCompetenceHorus(competenceHorus);
                    product.setCodGtin(codGTIN);


                    listProduct.add(product);

                }

            }

            // VERIFICO SE JA POSSUI A COMPETENCIA NO BANCO DE DADOS, SE SIM REMOVO
            List<CompetenceHorus> competenceHorusList = sector.getCompetenceHorusList();

            for(CompetenceHorus value: sector.getCompetenceHorusList()){

                if (value.getCompetence() == competence){
                    competenceHorusRepository.delete(value);
                }
            }

            competenceHorus.setProducts(listProduct);

            // ADICIONO A NOVA COMPETENCIA
            sector.getCompetenceHorusList().add(competenceHorus);

            // SALVO AS ALTERAÇÕES NO BANCO DE DADOS
            sectorRepository.save(sector);

            competenceHorusRepository.save(competenceHorus);

            productRepository.saveAll(listProduct);


            return listProduct;

        }catch (IOException e)
        {
            throw new IOException("Error to process the Excel file");
        }

    }

    //Função para gerar um arquivo TXT
    @Transactional
    public List<String> createTxt(String compString, String tenant) throws IOException {


        List<ProductTxt> productTxtList = new ArrayList<>();
        List<String> returnList = new ArrayList<>();

        entityManager.createNativeQuery("SET search_path TO \"" + tenant + "\"").executeUpdate();


        //Buscar no banco de dados a competência
        CompetenceHorus competenceHorus = competenceHorusRepository.findByCompetence(compString);

        System.out.println(competenceHorus.getCompetence());
        System.out.println(compString);




        List<Product> productList = competenceHorus.getProducts();

        // Altero as listas de produtos verificando se há produtos com GTIN repetidos, os repetidos eu somo em apenas um productTXT
        for (int i = 0; productList.size() > i; i++){

            Product product = productList.get(i);

            ProductTxt productTxt = new ProductTxt();

            productTxt.setAmount(product.getAmount());
            productTxt.setDescription(product.getDescription());
            productTxt.setCodGTIN(product.getCodGtin());
            productTxt.setUnit(product.getUnit());
            productTxt.setCnes(competenceHorus.getSector().getCnes());


            boolean vBoolean= true;

            if (!productTxtList.isEmpty()) {
                for (int d = 0; productTxtList.size() > d; d++) {

                    if (productTxt.getCodGTIN().equals(productTxtList.get(d).getCodGTIN())) {
                        productTxtList.get(d).setAmount(productTxtList.get(d).getAmount() + productTxt.getAmount());
                        vBoolean = true;
                        break;
                    } else {
                        vBoolean = false;
                    }
                }
            }else {
                productTxtList.add(productTxt);
            }

            if (!vBoolean){
                productTxtList.add(productTxt);
            }

        }



        //Aqui começo a gerar as Linhas do arquivo EstoqueFarmacia
            String entity = tenant;

            String competenceMonth = compString.substring(0,2);

            Random random = new Random();

            for (int i = 0; productTxtList.size() > i; i++){
                ProductTxt productTxt = productTxtList.get(i);
                String line = entity + competenceMonth + productTxt.getCnes();
                StringBuilder sb = new StringBuilder();
                String codGTIN = productTxt.getCodGTIN();

                /* Troca as letras do codGTIN por um numero aleatorio */
                for (int d = 0; d < codGTIN.length(); d++){


                    char c = codGTIN.charAt(d);
                    char v = '-';

                    if (Character.isLetter(c) || c == v){

                        int randomNumber = random.nextInt(10);
                        sb.append(randomNumber);

                    }else {
                        sb.append(c);
                    }
                }
                codGTIN = sb.toString();

                // Configuração do layout do arquivo Estoque farmacia
                if (codGTIN.length() > 14){
                    line = line + codGTIN.substring(0,13);
                }else{
                    line += codGTIN;
                }

                if (line.length() < 29){
                    line += " ".repeat(29 - line.length());
                }

                String description = productTxt.getDescription();

                if (description.length() > 60){
                    line += description.substring(0, 58) + " ".repeat(2);
                }else {
                    line += description;

                    line += " ".repeat(89 - line.length());
                }

                String unit = productTxt.getUnit();

                if (unit.length() > 10){
                    line += unit.substring(0,9);
                }else {
                    line += unit;
                    line += " ".repeat(99 - line.length());
                }

                String amount = String.valueOf(productTxt.getAmount());

                line += amount;
                line += " ".repeat( 114 - line.length());

                if(i +1 > productTxtList.size() && productTxtList.get(i+1) == null){

                    line += " ";
                }


                returnList.add(line);

            }



            //Retornar a lista de linhas
            return returnList;

    }
}
