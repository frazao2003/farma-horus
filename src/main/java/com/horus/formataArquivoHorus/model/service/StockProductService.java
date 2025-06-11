package com.horus.formataArquivoHorus.model.service;

import com.horus.formataArquivoHorus.model.entity.*;
import com.horus.formataArquivoHorus.model.entity.dto.EntryRequestDTO;
import com.horus.formataArquivoHorus.model.entity.dto.OutPutRequestDTO;
import com.horus.formataArquivoHorus.model.entity.dto.OutputDTO;
import com.horus.formataArquivoHorus.model.entity.dto.StockProductDTO;
import com.horus.formataArquivoHorus.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class StockProductService {

    @Autowired
    private StockProductRepository stockProductRepository;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private InProductService inProductService;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private OutputRepository outputRepository;

    @Autowired
    private OutputProductRepository outputProductRepository;

    @Autowired
    private EntryProductsRepository entryProductsRepository;

    //função para dar entrada
    public void entry(EntryRequestDTO entryRequestDTO, String id, String tenant){

        //chama o setor pelo cnes e ecode
        Sector sector = sectorService.getById(id);

        //pego a lista de produtos desse setor, e crio as variaveis necessarias
        List<StockProduct> stockProductList = sector.getStockProducts();

        Entry entry = new Entry();

        List<EntryProduct> entryList = new ArrayList<>();

        List<StockProductDTO> stockProductDTOList = entryRequestDTO.getStockProductDTOList();

        //itero sobre a lista de produtos recebidas
        for(StockProductDTO stockProductDTO: stockProductDTOList){

            boolean found = false;

            //verifico se já existe um produto com os mesmo atributos no banco de dados, se sim adiciono a quantidade a ele
            for(StockProduct value: stockProductList){

                if(Objects.equals(value.getInProduct().getId(), stockProductDTO.getIdProduct()) &&
                        Objects.equals(value.getCodLot(), stockProductDTO.getCodLot()) &&
                        value.getExpiationDate().compareTo(stockProductDTO.getExpiationDate()) == 0){




                    value.setAmount(value.getAmount() + stockProductDTO.getAmount());
                    found = true;

                    System.out.println("Se eu apareci fudeu");

                    //crio um entryProduct para salvar a quantidade que recebeu a entrada
                    EntryProduct entryProduct = new EntryProduct();

                    entryProduct.setCodLot(value.getCodLot());
                    entryProduct.setAmount(stockProductDTO.getAmount());
                    entryProduct.setEntry(entry);
                    entryProduct.setDescription(value.getInProduct().getDescription());
                    entryProduct.setUnit(value.getInProduct().getUnit());


                    entryList.add(entryProduct);
                    break;
                } else if (Objects.equals(value.getCodLot(), stockProductDTO.getCodLot())) {
                    throw new RuntimeException("CODLOT ALREADY REGISTERED");
                }
            }

            //se não encontro um produto igual, crio um novo
            if(!found){
                InProduct product = inProductService.getByID(stockProductDTO.getIdProduct());
                StockProduct stockProduct = new StockProduct();
                stockProduct.setInProduct(product);

                if(stockProductDTO.getAmount() > 0){
                    stockProduct.setAmount(stockProductDTO.getAmount());
                }else{
                    throw  new IllegalArgumentException("Amount invalid, product:" + product.getDescription());
                }

                stockProduct.setCodLot(stockProductDTO.getCodLot());

                if(stockProductDTO.getExpiationDate().after(new Date())){
                    stockProduct.setExpiationDate(stockProductDTO.getExpiationDate());
                }else{
                    throw new IllegalArgumentException("Expiation date expired, cod Lot: " + stockProductDTO.getCodLot() + "Description: " + product.getDescription());
                }

                stockProduct.setSector(sector);
                stockProduct.setCreatedAt(entryRequestDTO.getEntryDate());

                stockProductList.add(stockProduct);

                // e novamente crio um entryProduct para salvar a entrada
                EntryProduct entryProduct = new EntryProduct();
                entryProduct.setCodLot(stockProduct.getCodLot());
                entryProduct.setAmount(stockProductDTO.getAmount());
                entryProduct.setEntry(entry);
                entryProduct.setDescription(stockProduct.getInProduct().getDescription());
                entryProduct.setUnit(stockProduct.getInProduct().getUnit());
                entryList.add(entryProduct);


            }
        }


        //aqui persisto os dados
        try{
            stockProductRepository.saveAll(stockProductList);

            sector.setStockProducts(stockProductList);
            sectorRepository.save(sector);


            entry.setCreatedAt(entryRequestDTO.getEntryDate());
            entry.setEntryProducts(entryList);
            entry.setSector(sector);

            entryRepository.save(entry);

            entryProductsRepository.saveAll(entryList);

        }catch (RuntimeException e){
            throw new RuntimeException("ERROR TO SAVE THE ENTRY");
        }

    }

    //função para dar saída em uma lista de produtos vinculadas a um setor
    public void output(OutPutRequestDTO outputDTOS, String id){

        //chamo o setor, e crio as variaveis necessarias
        Sector sector = sectorService.getById(id);

        List<StockProduct> listProducts = sector.getStockProducts();

        List<OutputProduct> outPutList = new ArrayList<>();

        List<OutputDTO> outputDTOList = outputDTOS.getOutputDTOList();

        OutPut outPut = new OutPut();

        //verifico se o setor possui estoque
        if(listProducts.isEmpty()){
            throw new RuntimeException("THE SECTOR DON'T HAVE STOCK");
        }

        //itero sobre a lista de outputDTO
        for(OutputDTO outputDTO: outputDTOList){
            boolean  aBoolean = true;

            //itero sobre a lista de produtos do setor, e verifico se contem o outputDTO nessa lista
            for(StockProduct value: listProducts){

                if(outputDTO.getCodLot().equals(value.getCodLot())){
                    if( value.getExpiationDate().before(new Date()) || value.getExpiationDate().equals(new Date())){
                        throw new IllegalArgumentException("PRODUCT EXPIATED, PRODUCT: " + value.getCodLot() + " DESCRIPTION " + value.getInProduct().getDescription());
                    }if( value.getCreatedAt().after(outputDTOS.getOutputDate())){
                        throw new IllegalArgumentException("ENTRY DATE OF THE PRODUCT IS MINOR OF THE EXIT OUTPUT, PRODUCT: " + value.getCodLot() + " DESCRIPTION " + value.getInProduct().getDescription());
                    }if(value.getAmount() >= outputDTO.getAmount()){
                        value.setAmount(value.getAmount() - outputDTO.getAmount());

                        //crio um outputProduct para salvar quantos produtos sairam
                        OutputProduct outputProduct = new OutputProduct();
                        outputProduct.setAmount(outputDTO.getAmount());
                        outputProduct.setCodLot(outputDTO.getCodLot());
                        outputProduct.setOutput(outPut);
                        outputProduct.setDescription(value.getInProduct().getDescription());
                        outputProduct.setUnit(value.getInProduct().getUnit());

                        outPutList.add(outputProduct);
                    }else{
                        throw new IllegalArgumentException("AMOUNT INDISPOSITION, PRODUCT: " + value.getCodLot() + " DESCRIPTION " + value.getInProduct().getDescription() + " ACTUAL AMOUNT: " + value.getAmount() + " OUTPUT AMOUNT: " + outputDTO.getAmount());
                    }
                    aBoolean = false;
                    break;
                }
            }

            if(aBoolean){
                throw new RuntimeException("INVALID COD LOT: " + outputDTO.getCodLot());
            }
        }

        //persisto os dados
        try{
            sector.setStockProducts(listProducts);
            sectorRepository.save(sector);

            outPut.setCreatedAt(outputDTOS.getOutputDate());
            outPut.setOutputProducts(outPutList);
            outPut.setSector(sector);

            outputRepository.save(outPut);
            outputProductRepository.saveAll(outPutList);


        }catch (IllegalStateException e){

            throw new RuntimeException("ERROR TO SAVE THE OUTPUT");
        }
    }


    public List<StockProduct> getAllBySector(String id, String tenant){

        Sector sector = sectorService.getById(id);

        return sector.getStockProducts();

    }
}
