package com.example.usermanagementbackend.service;

import com.example.usermanagementbackend.entity.Produit;
import com.example.usermanagementbackend.entity.User;
import com.example.usermanagementbackend.repository.ProduitRepository;
import com.example.usermanagementbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    private static final Logger logger = LoggerFactory.getLogger(ProduitService.class);

    private final ProduitRepository produitRepository;
    private final UserRepository userRepository;

    public ProduitService(ProduitRepository produitRepository, UserRepository userRepository) {
        this.produitRepository = produitRepository;
        this.userRepository = userRepository;
    }

    public List<Produit> getAllProduits() {
        logger.info("Fetching all produits");
        List<Produit> produits = produitRepository.findAll();
        logger.info("Found {} produits", produits.size());
        return produits;
    }

    public Optional<Produit> getProduitById(Long id) {
        logger.info("Fetching produit with id: {}", id);
        Optional<Produit> produit = produitRepository.findById(id);
        logger.info("Produit found: {}", produit.isPresent());
        return produit;
    }

    public Produit createProduit(Produit produit) {
        logger.info("Creating produit: {}", produit.getNom());
        validateProduit(produit);
        Produit saved = produitRepository.save(produit);
        logger.info("Produit created with id: {}", saved.getId());
        return saved;
    }

    public Produit updateProduit(Long id, Produit updatedProduit) {
        logger.info("Updating produit with id: {}", id);
        return produitRepository.findById(id).map(produit -> {
            produit.setNom(updatedProduit.getNom());
            produit.setDescription(updatedProduit.getDescription());
            produit.setPrix(updatedProduit.getPrix());
            produit.setStock(updatedProduit.getStock());
            produit.setUser(updatedProduit.getUser());
            validateProduit(produit);
            Produit updated = produitRepository.save(produit);
            logger.info("Produit updated with id: {}", updated.getId());
            return updated;
        }).orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + id));
    }

    public void deleteProduit(Long id) {
        logger.info("Deleting produit with id: {}", id);
        if (!produitRepository.existsById(id)) {
            throw new RuntimeException("Produit non trouvé avec l'ID: " + id);
        }
        produitRepository.deleteById(id);
        logger.info("Produit deleted with id: {}", id);
    }

    private void validateProduit(Produit produit) {
        logger.info("Validating produit: {}", produit.getNom());
        if (produit.getPrix() < 0) {
            logger.error("Validation failed: Prix is negative: {}", produit.getPrix());
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        if (produit.getStock() < 0) {
            logger.error("Validation failed: Stock is negative: {}", produit.getStock());
            throw new IllegalArgumentException("Le stock ne peut pas être négatif");
        }
        if (produit.getUser() == null || produit.getUser().getId() == null) {
            logger.error("Validation failed: User is null or has no ID");
            throw new IllegalArgumentException("User is required for a produit");
        }
        User user = userRepository.findById(produit.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + produit.getUser().getId()));
        produit.setUser(user);
        logger.info("Produit validation passed");
    }

    public Produit saveProduit(Produit produit) {
        logger.info("Saving produit: {}", produit.getNom());
        validateProduit(produit);
        Produit saved = produitRepository.save(produit);
        logger.info("Produit saved with id: {}", saved.getId());
        return saved;
    }
}