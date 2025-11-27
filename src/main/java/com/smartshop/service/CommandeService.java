package com.smartshop.service;

import com.smartshop.dto.request.CommandeRequestDTO;
import com.smartshop.dto.response.CommandeResponseDTO;

public interface CommandeService {

    public CommandeResponseDTO createCommande(CommandeRequestDTO commandeRequestDTO);
}
