package com.smartshop.entity;

import com.smartshop.enums.StatutCommande;
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
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private Double sousTotal;

    @Column(nullable = false)
    private Double montantRemise;

    @Column(nullable = false)
    private int remise;

    @Column(nullable = false)
    private int TVA;

    @Column(nullable = false)
    private Double totalTTC;

    @Column(nullable = false)
    private Double totalAPayer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutCommande statutCommande;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    @Column(nullable = false)
    private int numeroPaiement;


    @ManyToOne
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<CommandeProduit> commandeProduits;

    @OneToMany(mappedBy = "commande")
    private List<Paiement> paiements;



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
