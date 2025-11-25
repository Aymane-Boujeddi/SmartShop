package com.smartshop.entity;

import com.smartshop.enums.NiveauFidelite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "clients")
public class Client {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(name = "niveau_fidelite")
    @Enumerated(EnumType.STRING)
    private NiveauFidelite niveauFidelite;


    @Column(nullable = false)
    private Double montantCumule;

    @Column(nullable = false)
    private int totalCommandes;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime datePremiereCommande;

    private LocalDateTime dateDerniereCommande;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "client")
    private List<Commande> commandes;


    @PrePersist
    public void setDateCreation(){
        this.dateCreation = LocalDateTime.now();
        this.montantCumule = 0.0;
        this.niveauFidelite = NiveauFidelite.BASIC;
        this.totalCommandes = 0;

    }





}
