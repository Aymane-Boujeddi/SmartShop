package com.smartshop.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "produits")
public class Produit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private Double prixUnitaire;

    @Column(nullable = false)
    private int stockDisponible;

    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    @OneToMany(mappedBy = "produit")
    private List<CommandeProduit> commandeProduits;





    @PrePersist
    public void dateCreationSet(){
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
        this.deleted = false;
    }

    @PreUpdate
    public void dateModificationSet(){
        this.dateModification = LocalDateTime.now();
    }





}
