package com.adel.jwt.repository;

import com.adel.jwt.model.Expense;
import com.adel.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    List<Expense> findByUserOrderByDateDesc(User user);
    
    @Query("SELECT e FROM Expense e WHERE e.user = :user ORDER BY e.date DESC LIMIT 5")
    List<Expense> findTop5ByUserOrderByDateDesc(@Param("user") User user);
    
    @Query("SELECT e.category AS category, SUM(e.amount) AS total " +
           "FROM Expense e WHERE e.user = :user GROUP BY e.category")
    List<Map<String, Object>> getCategoryTotals(@Param("user") User user);
    
    @Query("SELECT FUNCTION('MONTH', e.date) AS month, SUM(e.amount) AS total " +
           "FROM Expense e WHERE e.user = :user GROUP BY FUNCTION('MONTH', e.date)")
    List<Map<String, Object>> getMonthlyTotals(@Param("user") User user);
    
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user = :user")
    BigDecimal getTotalExpensesByUser(@Param("user") User user);
    
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
           "WHERE e.user = :user AND FUNCTION('MONTH', e.date) = FUNCTION('MONTH', CURRENT_DATE)")
    BigDecimal getMonthlyExpensesByUser(@Param("user") User user);
    
    // Add this method to fix the error
    Optional<Expense> findById(Long id);
}