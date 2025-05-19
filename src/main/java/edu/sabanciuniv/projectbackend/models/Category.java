package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Version
    @Column(name = "version", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long version = 0L;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (version == null) {
            version = 0L;
        }
    }

    public Long getVersion() {
        return version != null ? version : 0L;
    }

    public void setVersion(Long version) {
        this.version = version != null ? version : 0L;
    }

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
    @JsonIgnore
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

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
}
