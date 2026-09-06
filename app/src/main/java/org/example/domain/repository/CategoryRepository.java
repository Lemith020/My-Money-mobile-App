package org.example.domain.repository;

import java.util.List;
import org.example.domain.model.Category;
import org.example.domain.model.TransactionType;

public interface CategoryRepository {
    List<Category> getCategoriesByType(TransactionType type);
    long addCategory(Category category);
}
