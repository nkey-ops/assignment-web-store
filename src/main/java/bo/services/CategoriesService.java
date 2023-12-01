package bo.services;

import java.util.List;
import java.util.Objects;

import db.repositories.CategoriesRepository;

/**
 * CategoriesService
 */
public class CategoriesService {
    private final CategoriesRepository categoriesRepo;

    public CategoriesService(CategoriesRepository categoriesRepo) {
        Objects.requireNonNull(categoriesRepo);

        this.categoriesRepo = categoriesRepo;
    }

    /**
     * Checks wherther the category exists
     * @param categoryName  
     * @return whether tje category exists
     */
    public boolean existsByName(String categoryName) {
        Objects.requireNonNull(categoryName);
        return categoriesRepo.existsByName(categoryName);
    }

    /**
     * Creates a category with privide name
     * @param categoryName the name of the category to create
     */
    public void createCategory(String categoryName) {
        Objects.requireNonNull(categoryName);

        if(categoriesRepo.existsByName(categoryName))
            throw new IllegalArgumentException("The category already exists");
        
        categoriesRepo.createCategory(categoryName);
    }

    /**
     * @return a String list of all categoriees
     */
    public List<String> getAllCategories() {
        return categoriesRepo.getAllCategories();
    }
}
