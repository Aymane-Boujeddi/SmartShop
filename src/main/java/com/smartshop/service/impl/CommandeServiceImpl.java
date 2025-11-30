package com.smartshop.service.impl;

import com.smartshop.dto.request.CommandeRequestDTO;
import com.smartshop.dto.response.CommandeResponseDTO;
import com.smartshop.entity.*;
import com.smartshop.enums.NiveauFidelite;
import com.smartshop.enums.StatutCommande;
import com.smartshop.exception.CannotDeleteException;
import com.smartshop.exception.StockInsuffisantException;
import com.smartshop.mapper.CommandeMapper;
import com.smartshop.repository.*;
import com.smartshop.service.CommandeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {


    private final CommandeRepository commandeRepository;

    private final ProduitRepository produitRepository;

    private final ClientRepository clientRepository;

    private final CommandeMapper commandeMapper;



    @Transactional
    @Override
    public CommandeResponseDTO createCommande(CommandeRequestDTO commandeRequestDTO) {
        Commande commande = commandeMapper.toEntity(commandeRequestDTO);

        Client client = getClientById(commandeRequestDTO.getClientId());

       int TVA = commandeRequestDTO.getTva();



        List<CommandeItem> commandeItemList = commandeRequestDTO.getCommandeList()
                .stream().map(commandeItemRequestDTO -> {
                    Produit produit = getProduitById(commandeItemRequestDTO.getProduitId());
                    if(commandeItemRequestDTO.getQuantite() > produit.getStockDisponible()){
                        throw new StockInsuffisantException(
                                "Insufficient stock for product: " + produit.getNom()
                                + " . ID : " + produit.getId()
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
                commandeRequestDTO.getCodePromo(),
                commandeRequestDTO.getClientId());

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

    @Transactional
    @Override
    public Map<String , Object> deleteCommande(Long id) {
        Commande commande = findCommandeById(id);
        canDelete(commande);
        Map<String ,Object> resposne = new HashMap<>();
        CommandeResponseDTO responseDTO = commandeMapper.toResponseDto(commande);

        commande.getCommandeItems().forEach(commandeItem -> {
            Produit produit = getProduitById(commandeItem.getProduit().getId());
            produit.setStockDisponible(produit.getStockDisponible() + commandeItem.getQuantite());
            produitRepository.save(produit);
        });
        commandeRepository.delete(commande);
        resposne.put("Message" , "Commande Deleted Successfully");
        resposne.put("Deleted Commande" , responseDTO);
        return resposne;
    }


    @Transactional
    @Override
    public CommandeResponseDTO updateCommandeStatutConfirmed(Long id) {
        Commande commande = findCommandeById(id);
        canConfirme(commande);


        Client client = commande.getClient();
        if(client.getTotalCommandes() == 0 ){
            client.setDatePremiereCommande(LocalDateTime.now());
        }
        client.setDateDerniereCommande(LocalDateTime.now());
        client.setMontantCumule(commande.getTotalTTC() + client.getMontantCumule());
        client.setTotalCommandes(client.getTotalCommandes() + 1);
        client.setNiveauFidelite(updateNiveauFidelite(client));

        Client savedClient = clientRepository.save(client);

        commande.setStatutCommande(StatutCommande.CONFIRMED);

        commande.setClient(savedClient);

        Commande savedCommande = commandeRepository.save(commande);

        return commandeMapper.toResponseDto(savedCommande);
    }

    @Transactional
    @Override
    public CommandeResponseDTO updateCommandeStatutCanceled(Long id) {
        Commande commande = findCommandeById(id);
        canCancel(commande);
        commande.getCommandeItems().forEach(item -> {
            Produit produit = getProduitById(item.getProduit().getId());
            produit.setStockDisponible(produit.getStockDisponible() + item.getQuantite());
            produitRepository.save(produit);
        });
        commande.setStatutCommande(StatutCommande.CANCELED);
        Commande canceledCommande = commandeRepository.save(commande);

        return commandeMapper.toResponseDto(canceledCommande);
    }


    // --------------------- Helper Methods (private)


    private Commande findCommandeById(Long id){
        return commandeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Commande not found with this id :" + id));
    }
    private Client getClientById(Long id){

        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with this id : "+ id));
    }
    private Produit getProduitById(Long id){
        return produitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this id : " + id));
    }
    private int getTotalRemise(Double sousTotal, NiveauFidelite niveauFidelite,String codePromo,Long clientId){
                Map<String , Object> remiseAndThreshold = getRemiseAndThreshholdFromNiveauFidelite(niveauFidelite);
                int fideliteRemise = (int) remiseAndThreshold.get("remise");
                double fideliteThreshold = (double) remiseAndThreshold.get("threshold");
                int remise = 0;
                if(codePromo != null && !isPromoCodeUsed(codePromo,clientId)){
                    remise += 5 ;
                }
                 if(sousTotal >= fideliteThreshold){
                     remise += fideliteRemise;
                 }
                 return remise;
    }
    private boolean isPromoCodeUsed(String promoCode,Long id){
        Client client = getClientById(id);
        return commandeRepository.existsByCodePromoAndClient(promoCode,client);
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
    private void canDelete(Commande commande){
        List<Payment> payments = commande.getPayments();
        if(!payments.isEmpty()){
            throw new CannotDeleteException("Cannot delete Commande because it has payment");
        } else if (!commande.getStatutCommande().equals(StatutCommande.PENDING)) {
            throw new CannotDeleteException("Cannot delete Commande because it's status is : " + commande.getStatutCommande());        }

    }
    private void canConfirme(Commande commande){
        Double montantRestant = commande.getMontantRestant();

        if(!commande.getStatutCommande().equals(StatutCommande.PENDING)){
            throw new IllegalArgumentException("Cannot confirme Commande . Only confirme commande with status(Pending) . This Commande status : " + commande.getStatutCommande() );
        }

        if (montantRestant > 0 ){
            throw new IllegalArgumentException("Cannot confirme this commande because it is not fully paid . Ammount to pay : " + montantRestant);
        }

    }
    private void canCancel(Commande commande){
        if(!commande.getStatutCommande().equals(StatutCommande.PENDING)){
            throw new CannotDeleteException("Cannot Cancel Commande with status " + commande.getStatutCommande());
        }
        Double amountPaid = commande.getTotalTTC() - commande.getMontantRestant();
        if(!commande.getPayments().isEmpty()){
            throw new CannotDeleteException("Cannot Cancel Commande  because it has payments : " + amountPaid);
        }
    }
    private NiveauFidelite updateNiveauFidelite(Client client){
        int totalCommande = client.getTotalCommandes();
        Double totalCumule = client.getMontantCumule();
        if (totalCommande >= 20 || totalCumule >= 15000) {
            return NiveauFidelite.PLATINUM;
        } else if (totalCommande >= 10 || totalCumule >= 5000) {
            return NiveauFidelite.GOLD;
        } else if (totalCommande >= 3 || totalCumule >= 1000) {
            return NiveauFidelite.SILVER;
        } else {
            return NiveauFidelite.BASIC;
        }
    }







}
