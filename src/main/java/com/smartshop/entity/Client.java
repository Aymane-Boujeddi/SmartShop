package com.smartshop.entity;

import com.smartshop.enums.NiveauFidelite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private NiveauFidelite niveauFidelite;


    private Double montantCumule;

    private int totalCommandes;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;





}
