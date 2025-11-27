package com.smartshop.entity;

import com.smartshop.enums.StatutCommande;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // le total de la commande avant les remises et tva
    @Column(nullable = false)
    private Double sousTotal;

    // montant de reduction ex : 100 dh
    @Column(nullable = false)
    private Double montantRemise;

    // taux de reduction ex : 5%
    @Column(nullable = false)
    private int remise;


    // le taux tva : 20% updatable
    @Column(nullable = false)
    private int TVA;


    @Pattern(regexp = "PROMO-[A-Z0-9]{4}")
    private String codePromo;

    // le total after tax calculated : totalPreTax + (totalPretax * TVA)
    @Column(nullable = false)
    private Double totalTTC;

    // le total avant implementation du tax
    @Column(nullable = false)
    private Double totalPreTax;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutCommande statutCommande;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;


    // pour le tracking de numero de paiment dans l'entite paiement
    @Column(nullable = false)
    private int numeroPaiement;

    // montant restant a payer pour tracker le reste de paiment
    @Column(nullable = false)
    private Double montantRestant;


    @ManyToOne
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<CommandeItem> commandeItems;

    @OneToMany(mappedBy = "commande")
    private List<Payment> payments;



    @PrePersist
    public void dateCreationSet(){
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
        this.numeroPaiement = 0;
    }

    @PreUpdate
    public void setDateModification(){
        this.dateModification = LocalDateTime.now();
    }
}
