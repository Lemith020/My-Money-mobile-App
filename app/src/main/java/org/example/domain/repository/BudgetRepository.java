package org.example.domain.repository;

import org.example.domain.model.Budget;

public interface BudgetRepository {
    Budget getBudget();
    void saveBudget(Budget budget);
}
