package com.smartshop.mapper;

import com.smartshop.dto.request.CommandeItemRequestDTO;
import com.smartshop.dto.response.CommandeItemResponseDTO;
import com.smartshop.entity.CommandeItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandeItemMapper {


    @Mapping(target = "produitId", source = "produit.id")
    @Mapping(target = "produitNom", source = "produit.nom")
    CommandeItemResponseDTO toResponseDto(CommandeItem commandeItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prixUnitaire", ignore = true)
    @Mapping(target = "totalLigne", ignore = true)
    @Mapping(target = "commande", ignore = true)
    @Mapping(target = "produit", ignore = true)
    CommandeItem toEntity(CommandeItemRequestDTO commandeItemRequestDTO);
}
