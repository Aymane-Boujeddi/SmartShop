package com.smartshop.controller;

import com.smartshop.dto.request.CommandeRequestDTO;
import com.smartshop.dto.response.CommandeResponseDTO;
import com.smartshop.service.CommandeService;
import com.smartshop.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;


    @PostMapping("/commande")
    public ResponseEntity<CommandeResponseDTO> createCommande(@Valid @RequestBody CommandeRequestDTO commandeRequestDTO, HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(commandeService.createCommande(commandeRequestDTO));
    }

    @GetMapping("/commande")
    public ResponseEntity<List<CommandeResponseDTO>> getAllCommande(HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(commandeService.getAllCommande());
    }

    @GetMapping("/commande/{id}")
    public ResponseEntity<CommandeResponseDTO> getCommanedById(@PathVariable Long id,HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(commandeService.getCommandeById(id));
    }

}
