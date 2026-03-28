package com.smartshop.service.impl;

import com.smartshop.dto.request.ProduitRequestDTO;
import com.smartshop.dto.response.ProduitResponseDTO;
import com.smartshop.entity.Commande;
import com.smartshop.entity.CommandeItem;
import com.smartshop.entity.Produit;
import com.smartshop.mapper.ProduitMapper;
import com.smartshop.repository.CommandeRepository;
import com.smartshop.repository.ProduitRepository;
import com.smartshop.service.CommandeService;
import com.smartshop.service.ProduitService;
import com.smartshop.specification.ProduitSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;

    private final CommandeRepository commandeRepository;

    private final ProduitMapper produitMapper;
    private final CommandeService commandeService;

    @Override
    public ProduitResponseDTO createProduit(ProduitRequestDTO produitRequestDTO) {
        Produit produit = produitMapper.toEntity(produitRequestDTO);

        Produit savedProduit = produitRepository.save(produit);


        return produitMapper.toResponseDto(savedProduit);
    }

    @Override
    public ProduitResponseDTO getOneProductById(Long id) {
        Produit produit = getProductById(id);
        return produitMapper.toResponseDto(produit);
    }

    @Override
    public List<ProduitResponseDTO> getAllProducts(
            Boolean deleted,
            String nom,
            Integer minStock,
            Integer maxStock,
            Double minPrice,
            Double maxPrice,
            LocalDateTime startCreationDate ,
            LocalDateTime endCreationDate ,
            Integer page ,
            Integer size

            ) {
        Specification<Produit> specification = ProduitSpecification.isDeleted(deleted)
                .and(ProduitSpecification.createdBetween(startCreationDate,endCreationDate))
                .and(ProduitSpecification.hasName(nom))
                .and(ProduitSpecification.hasStockBetween(minStock,maxStock))
                .and(ProduitSpecification.hasPriceBetween(minPrice,maxPrice));

        Pageable pageable = PageRequest.of(page,size);




        return produitRepository
                .findAll(specification,pageable)
                .stream()
                .map(produitMapper::toResponseDto)
                .toList();


    }



    @Override
    public ProduitResponseDTO updateProductById(Long id,ProduitRequestDTO produitRequestDTO) {
        Produit produit = getProductById(id);

        produit.setNom(produitRequestDTO.getNom());
        produit.setPrixUnitaire(produitRequestDTO.getPrixUnitaire());
        produit.setStockDisponible(produitRequestDTO.getStockDisponible());

        Produit savedProduit = produitRepository.save(produit);


        return produitMapper.toResponseDto(savedProduit);
    }

    @Override
    public ProduitResponseDTO deleteProductById(Long id) {
        Produit produit = getProductById(id);

        if(produit.getCommandeItems().isEmpty()){
            produitRepository.delete(produit);
            return produitMapper.toResponseDto(produit);
        }else {
            produit.setDeleted(true);
            Produit deletedProduct = produitRepository.save(produit);
            return produitMapper.toResponseDto(deletedProduct);

        }
    }

    public List<ProduitResponseDTO> produitVend(){
        List<Produit> produits = new ArrayList<>();
        commandeRepository.findAll().forEach(commande -> {
           commande.getCommandeItems().forEach(commandeItem -> {
               produits.add(commandeItem.getProduit());
           });
        });

        return produits.stream().map(produitMapper::toResponseDto).toList();
    }




    private Produit getProductById(Long id){
        return produitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this id :" + id));
    }

    // ------------------ mis




}
