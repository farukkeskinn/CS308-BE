package edu.sabanciuniv.projectbackend.models;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // It's an INT AUTO_INCREMENT
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    // Self-referencing parent category
    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    // If you want a bidirectional relationship to get subcategories:
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> subCategories = new ArrayList<>();

    // Constructors, Getters, Setters
    public Integer getCategoryId() {
        return categoryId;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }
}
