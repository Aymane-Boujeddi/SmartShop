package com.smartshop.specification;

import com.smartshop.entity.Produit;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ProduitSpecification {

    public static Specification<Produit> hasName(String nom) {
        return (root, query, criteriaBuilder) ->
                nom == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), "%" + nom.toLowerCase() + "%");
    }

    public static Specification<Produit> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice == null) return criteriaBuilder.lessThanOrEqualTo(root.get("prixUnitaire"), maxPrice);
            if (maxPrice == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("prixUnitaire"), minPrice);
            return criteriaBuilder.between(root.get("prixUnitaire"), minPrice, maxPrice);
        };
    }

    public static Specification<Produit> hasStockBetween(Integer minStock, Integer maxStock) {
        return (root, query, criteriaBuilder) -> {
            if (minStock == null && maxStock == null) return null;
            if (minStock == null) return criteriaBuilder.lessThanOrEqualTo(root.get("stockDisponible"), maxStock);
            if (maxStock == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("stockDisponible"), minStock);
            return criteriaBuilder.between(root.get("stockDisponible"), minStock, maxStock);
        };
    }

    public static Specification<Produit> isDeleted(Boolean deleted) {
        return (root, query, criteriaBuilder) ->
                deleted == null ? null : criteriaBuilder.equal(root.get("deleted"), deleted);
    }

    public static Specification<Produit> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) return null;
            if (startDate == null) return criteriaBuilder.lessThanOrEqualTo(root.get("dateCreation"), endDate);
            if (endDate == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreation"), startDate);
            return criteriaBuilder.between(root.get("dateCreation"), startDate, endDate);
        };
    }






}
