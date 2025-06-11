package com.horus.formataArquivoHorus.model.service;

import com.horus.formataArquivoHorus.model.entity.InProduct;
import com.horus.formataArquivoHorus.model.entity.Sector;
import com.horus.formataArquivoHorus.model.entity.dto.InProductDTO;
import com.horus.formataArquivoHorus.model.repository.InProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InProductService {

    @Autowired
    private InProductRepository inProductRepository;

    @Autowired
    private ModelMapper modelMapper;


    //salvo um inProduc
    public void insertInProduct(InProductDTO inProductDTO){

        List<InProduct> inProductList = inProductRepository.findAll();

        //verifico se já existe
        for(InProduct value: inProductList){

            if(Objects.equals(inProductDTO.getDescription(), value.getDescription()) && Objects.equals(inProductDTO.getUnit(), value.getUnit())
            && Objects.equals(inProductDTO.getComposicao(), value.getComposicao()) && Objects.equals(inProductDTO.getManufacturer(), value.getManufacturer())){

                throw new IllegalArgumentException("PRODUCT ALREADY REGISTERED");
            }
        }

        InProduct inProduct = new InProduct();

        inProduct = modelMapper.map(inProductDTO, InProduct.class);

        inProductRepository.save(inProduct);
    }

    //chamo todos os InProduct
    public List<InProduct> getALl(){

        List<InProduct> listProduct = inProductRepository.findAll();

        if(listProduct.isEmpty()){
            throw new EntityNotFoundException("Error");
        }

        return listProduct;
    }

    //chamo por id
    public InProduct getByID(Long id){
        Optional<InProduct> inProduct = inProductRepository.findById(String.valueOf(id));

        if (inProduct.isPresent()){
            return  inProduct.get();
        }else{
            throw new IllegalArgumentException("PRODUCT NOT FOUND");
        }
    }
}
