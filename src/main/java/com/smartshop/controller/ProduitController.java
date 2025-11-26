package com.smartshop.controller;


import com.smartshop.dto.request.ProduitRequestDTO;
import com.smartshop.dto.response.ProduitResponseDTO;
import com.smartshop.service.ProduitService;
import com.smartshop.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProduitController {


    private final ProduitService produitService;


    @PostMapping("/produit")
    public ResponseEntity<ProduitResponseDTO> createProduct(@Valid @RequestBody ProduitRequestDTO produitRequestDTO, HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(produitService.createProduit(produitRequestDTO));

    }
}
