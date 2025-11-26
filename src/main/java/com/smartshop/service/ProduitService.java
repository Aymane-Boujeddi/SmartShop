package com.smartshop.service;

import com.smartshop.dto.request.ProduitRequestDTO;
import com.smartshop.dto.response.ProduitResponseDTO;

public interface ProduitService {
    public ProduitResponseDTO createProduit(ProduitRequestDTO produitRequestDTO);
    public ProduitResponseDTO getOneProductById(Long id);
}
