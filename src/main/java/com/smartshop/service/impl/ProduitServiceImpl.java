package com.smartshop.service.impl;

import com.smartshop.dto.request.ProduitRequestDTO;
import com.smartshop.dto.response.ProduitResponseDTO;
import com.smartshop.entity.Produit;
import com.smartshop.mapper.ProduitMapper;
import com.smartshop.repository.ProduitRepository;
import com.smartshop.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
