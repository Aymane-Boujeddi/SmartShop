package com.smartshop.mapper;


import com.smartshop.dto.request.CommandeRequestDTO;
import com.smartshop.dto.response.CommandeResponseDTO;
import com.smartshop.entity.Commande;
import com.smartshop.enums.StatutCommande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",uses = {CommandeItemMapper.class})
public interface CommandeMapper {



    @Mapping(target = "statutCommande",source = "statutCommande",qualifiedByName = "statutToString")
    @Mapping(target = "clientId",source = "client.id")
    @Mapping(target = "clientNom",source = "client.nom")
    @Mapping(target = "clientEmail",source = "client.email")
    @Mapping(target = "commandeItems",source = "commandeItems")
    CommandeResponseDTO toResponseDto(Commande commande);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sousTotal", ignore = true)
    @Mapping(target = "montantRemise", ignore = true)
    @Mapping(target = "remise", ignore = true)
    @Mapping(target = "totalTTC", ignore = true)
    @Mapping(target = "totalPreTax", ignore = true)
    @Mapping(target = "statutCommande", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "numeroPaiement", ignore = true)
    @Mapping(target = "montantRestant", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "commandeItems", ignore = true)
    @Mapping(target = "paiements", ignore = true)
    Commande toEntity(CommandeRequestDTO commandeRequestDTO);

    @Named("statutToString")
    default String statutToString(StatutCommande statut) {
        return statut.toString();
    }

}
