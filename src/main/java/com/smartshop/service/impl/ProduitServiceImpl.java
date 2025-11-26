package com.smartshop.service.impl;

import com.smartshop.dto.request.ProduitRequestDTO;
import com.smartshop.dto.response.ProduitResponseDTO;
import com.smartshop.entity.Produit;
import com.smartshop.mapper.ProduitMapper;
import com.smartshop.repository.ProduitRepository;
import com.smartshop.service.ProduitService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;

    private final ProduitMapper produitMapper;

    @Override
    public ProduitResponseDTO createProduit(ProduitRequestDTO produitRequestDTO) {
        Produit produit = produitMapper.toEntity(produitRequestDTO);

        Produit savedProduit = produitRepository.save(produit);


        return produitMapper.toResponseDto(savedProduit);
    }

    @Override
    public ProduitResponseDTO getOneProductById(Long id) {
        Produit produit = getProductById(id);
        return produitMapper.toResponseDto(produit);
    }

    @Override
    public List<ProduitResponseDTO> getAllProducts() {
        List<Produit> produits = produitRepository.findAll();

        return produits.stream().map(produitMapper::toResponseDto).toList();
    }


    private Produit getProductById(Long id){
        return produitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this id :" + id));
    }


}
