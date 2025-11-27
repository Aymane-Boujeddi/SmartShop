package com.smartshop.entity;


import com.smartshop.enums.StatutPaiement;
import com.smartshop.enums.TypePaiement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "paiements")
public class Paiement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private int numeroPaiement;

    @Column(nullable = false)
    private Double montant;

    private String reference;

    private String banque;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutPaiement statutPaiement;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypePaiement typePaiement;

    private LocalDateTime datePaiement;

    private LocalDateTime dateEncaissement;

    @ManyToOne
    @JoinColumn(name = "commande_id",nullable = false)
    private Commande commande;

    @PrePersist
    public void dateInit(){
        this.datePaiement = LocalDateTime.now();
    }



}
