package com.smartshop.repository;

import com.smartshop.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProduitRepository extends JpaRepository<Produit, Long> , JpaSpecificationExecutor<Produit> {
}
