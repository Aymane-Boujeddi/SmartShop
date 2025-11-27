package com.smartshop.service.impl;

import com.smartshop.dto.request.CommandeRequestDTO;
import com.smartshop.dto.response.CommandeResponseDTO;
import com.smartshop.entity.*;
import com.smartshop.enums.NiveauFidelite;
import com.smartshop.enums.Role;
import com.smartshop.enums.StatutCommande;
import com.smartshop.exception.StockInsuffisantException;
import com.smartshop.exception.UserNotFoundException;
import com.smartshop.mapper.CommandeMapper;
import com.smartshop.repository.CommandeRepository;
import com.smartshop.repository.ProduitRepository;
import com.smartshop.repository.UserRepository;
import com.smartshop.service.CommandeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {

    private final UserRepository userRepository;

    private final CommandeRepository commandeRepository;

    private final ProduitRepository produitRepository;

    private final CommandeMapper commandeMapper;


    @Transactional
    @Override
    public CommandeResponseDTO createCommande(CommandeRequestDTO commandeRequestDTO) {
        Commande commande = commandeMapper.toEntity(commandeRequestDTO);

        Client client = getUserById(commandeRequestDTO.getClientId()).getClient();

       int TVA = commandeRequestDTO.getTva();



        List<CommandeItem> commandeItemList = commandeRequestDTO.getCommandeList()
                .stream().map(commandeItemRequestDTO -> {
                    Produit produit = getProduitById(commandeItemRequestDTO.getProduitId());
                    if(commandeItemRequestDTO.getQuantite() > produit.getStockDisponible()){
                        throw new StockInsuffisantException(
                                "Insufficient stock for product: " + produit.getNom()
                        );
                    }
                    return CommandeItem.builder()
                            .quantite(commandeItemRequestDTO.getQuantite())
                            .prixUnitaire(produit.getPrixUnitaire())
                            .totalLigne(commandeItemRequestDTO.getQuantite() * produit.getPrixUnitaire())
                            .produit(produit)

                            .build();
                }).toList();


        Double sousTotal = commandeItemList.stream()
                .mapToDouble(CommandeItem::getTotalLigne)
                .sum();


        int remise = getTotalRemise(sousTotal,
                client.getNiveauFidelite(),
                commandeRequestDTO.getCodePromo());

        Double montantRemise = sousTotal * remise / 100;
        Double montantPreTax = sousTotal - montantRemise;

        Double totalTTC = montantPreTax + (montantPreTax * TVA / 100.0);




        commande.setSousTotal(sousTotal);
        commande.setRemise(remise);
        commande.setMontantRemise(montantRemise);
        commande.setTotalPreTax(montantPreTax);
        commande.setTotalTTC(totalTTC);
        commande.setClient(client);
        commande.setTVA(TVA);
        commande.setMontantRestant(totalTTC);
        commande.setCommandeItems(commandeItemList);
        commande.setStatutCommande(StatutCommande.PENDING);


        commande.getCommandeItems().forEach(item -> item.setCommande(commande));

        Commande savedCommande = commandeRepository.save(commande);

        commandeItemList.forEach(item -> {
            Produit produit = item.getProduit();
            produit.setStockDisponible(produit.getStockDisponible() - item.getQuantite());
            produitRepository.save(produit);
        });

        return commandeMapper.toResponseDto(savedCommande);
    }

    @Override
    public List<CommandeResponseDTO> getAllCommande() {

        return commandeRepository.findAll()
                .stream()
                .map(commandeMapper::toResponseDto)
                .toList();
    }

    @Override
    public CommandeResponseDTO getCommandeById(Long id) {

        return commandeMapper.toResponseDto(findCommandeById(id));
    }

    @Override
    public List<CommandeResponseDTO> getPayedCommandes() {
        List<Commande> payedCommandes = commandeRepository.findAllByMontantRestant(0.0);
        return payedCommandes.stream().map(commandeMapper::toResponseDto).toList();
    }

    @Override
    public Map<String , Object> deleteCommande(Long id) {
        Commande commande = findCommandeById(id);
        Map<String ,Object> resposne = new HashMap<>();
        CommandeResponseDTO responseDTO = commandeMapper.toResponseDto(commande);
        commandeRepository.delete(commande);
        resposne.put("Message" , "Commande Deleted Successfully");
        resposne.put("Deleted Commande" , responseDTO);
        return resposne;
    }


    // --------------------- Helper Methods (private)


    private Commande findCommandeById(Long id){
        return commandeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Commande not found with this id :" + id));
    }
    private User getUserById(Long id){
        User client =  userRepository.findUserByIdAndRole(id, Role.CLIENT);

        if(client == null){
            throw  new UserNotFoundException("Client not found with this id : "+ id);
        }
        return client;
    }
    private Produit getProduitById(Long id){
        return produitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this id : " + id));
    }
    private int getTotalRemise(Double sousTotal, NiveauFidelite niveauFidelite,String codePromo){
                Map<String , Object> remiseAndThreshold = getRemiseAndThreshholdFromNiveauFidelite(niveauFidelite);
                int fideliteRemise = (int) remiseAndThreshold.get("remise");
                double fideliteThreshold = (double) remiseAndThreshold.get("threshold");
                int remise = 0;
                if(codePromo != null ){
                    remise += 5 ;
                }
                 if(sousTotal >= fideliteThreshold){
                     remise += fideliteRemise;
                 }
                 return remise;
    }
    private Map<String , Object> getRemiseAndThreshholdFromNiveauFidelite(NiveauFidelite niveauFidelite){
        Map<String , Object> remiseAndThreshhold = new HashMap<>();
        int remise = 0;
        double threshold = 0.0;

            switch (niveauFidelite){
                case BASIC -> {

                }
                case SILVER -> {
                   remise = 5;
                   threshold = 500;
                }
                case GOLD -> {
                    remise = 10;
                    threshold = 800;
                }
                case PLATINUM -> {
                    remise = 15;
                    threshold = 1200;
                }
            }
            remiseAndThreshhold.put("remise",remise);
            remiseAndThreshhold.put("threshold",threshold);
            return remiseAndThreshhold;
    }




}
