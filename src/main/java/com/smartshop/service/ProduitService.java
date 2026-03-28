package com.smartshop.service;

import com.smartshop.dto.request.ProduitRequestDTO;
import com.smartshop.dto.response.ProduitResponseDTO;
import com.smartshop.entity.Produit;

import java.time.LocalDateTime;
import java.util.List;

public interface ProduitService {
    public ProduitResponseDTO createProduit(ProduitRequestDTO produitRequestDTO);
    public ProduitResponseDTO getOneProductById(Long id);
    public List<ProduitResponseDTO> getAllProducts( Boolean deleted,  String nom ,Integer minStock, Integer maxStock,
                                                    Double minPrice, Double maxPrice,
                                                    LocalDateTime startCreationDate , LocalDateTime endCreationDate ,
                                                    Integer page , Integer size);
    public ProduitResponseDTO updateProductById(Long id,ProduitRequestDTO produitRequestDTO);
    public ProduitResponseDTO deleteProductById(Long id);
}
