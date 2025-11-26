package com.smartshop.controller;


import com.smartshop.dto.request.ProduitRequestDTO;
import com.smartshop.dto.response.ProduitResponseDTO;
import com.smartshop.service.ProduitService;
import com.smartshop.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProduitController {


    private final ProduitService produitService;


    @PostMapping("/produit")
    public ResponseEntity<ProduitResponseDTO> createProduct(@Valid @RequestBody ProduitRequestDTO produitRequestDTO, HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(produitService.createProduit(produitRequestDTO));

    }

    @GetMapping("/produit/{id}")
    public ResponseEntity<ProduitResponseDTO> getOneProductById(@PathVariable Long id,HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(produitService.getOneProductById(id));
    }

    @GetMapping("/produits")
    public ResponseEntity<List<ProduitResponseDTO>> getAllProducts(HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(produitService.getAllProducts());
    }
}
