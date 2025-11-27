package com.smartshop.service;

import com.smartshop.dto.request.CommandeRequestDTO;
import com.smartshop.dto.response.CommandeResponseDTO;

import java.util.List;

public interface CommandeService {

    public CommandeResponseDTO createCommande(CommandeRequestDTO commandeRequestDTO);
    public List<CommandeResponseDTO> getAllCommande();
    public CommandeResponseDTO getCommandeById(Long id);
    public List<CommandeResponseDTO> getPayedCommandes();
}
