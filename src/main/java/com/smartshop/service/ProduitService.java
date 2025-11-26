package com.smartshop.service;

import com.smartshop.dto.request.ProduitRequestDTO;
import com.smartshop.dto.response.ProduitResponseDTO;

import java.util.List;

public interface ProduitService {
    public ProduitResponseDTO createProduit(ProduitRequestDTO produitRequestDTO);
    public ProduitResponseDTO getOneProductById(Long id);
    public List<ProduitResponseDTO> getAllProducts();
    public ProduitResponseDTO updateProductById(Long id,ProduitRequestDTO produitRequestDTO);
}
