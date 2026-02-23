package com.salescope.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
	
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double price;

    private String image;

    private String category;

    @Column(name = "seller_id")
    private Long sellerId;

    
    @Column(name = "active")
    private Boolean active = true;
    
    
    public Product() {}

    // ====== Getters ======

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public Long getSellerId() {
        return sellerId;
    }

    // ====== Setters ======

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    
    
}