package com.smartshop.controller;


import com.smartshop.dto.request.ProduitRequestDTO;
import com.smartshop.dto.response.ProduitResponseDTO;
import com.smartshop.service.ProduitService;
import com.smartshop.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<List<ProduitResponseDTO>> getAllProducts(
            HttpSession session,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock,
            @RequestParam(defaultValue = "false") Boolean deleted,
            @RequestParam(required = false)  LocalDateTime startCreationDate,
            @RequestParam(required = false)  LocalDateTime endCreationDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){


        SecurityUtil.checkAdmin(session);

        return ResponseEntity.ok(produitService
                .getAllProducts(deleted,nom,minStock,maxStock,minPrice,
                        maxPrice,startCreationDate,endCreationDate,page,size));
    }

    @PutMapping("/produit/{id}")
    public ResponseEntity<ProduitResponseDTO> updateProduit(
            @PathVariable Long id,
            @Valid @RequestBody ProduitRequestDTO produitRequestDTO,
            HttpSession session
            ){

        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(produitService.updateProductById(id,produitRequestDTO));
    }

    @DeleteMapping("/produit/{id}")
    public ResponseEntity<ProduitResponseDTO> deleteProduit(@PathVariable Long id , HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(produitService.deleteProductById(id));
    }



}
