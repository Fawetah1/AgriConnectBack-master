package com.example.usermanagementbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private double prix;
    private int stock;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required for a produit")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Allow deserialization, prevent serialization
    private User user;

    public Produit() {}  // No-arg constructor
    @OneToMany(mappedBy = "produit")
    @JsonIgnore // Important to prevent infinite recursion
    private List<LigneCommande> ligneCommandes;


    // Getters & Setters manually (or keep @Getter/@Setter from Lombok)
}

