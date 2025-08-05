package com.adel.jwt.service;

import com.adel.jwt.model.Expense;
import com.adel.jwt.model.User;
import com.adel.jwt.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }
    
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
    
    public List<Expense> getAllExpensesByUser(User user) {
        return expenseRepository.findByUserOrderByDateDesc(user);
    }
    
    public List<Expense> getRecentExpenses(User user) {
        return expenseRepository.findTop5ByUserOrderByDateDesc(user);
    }
    
    public List<Map<String, Object>> getCategoryTotals(User user) {
        return expenseRepository.getCategoryTotals(user);
    }
    
    public List<Map<String, Object>> getMonthlyTotals(User user) {
        return expenseRepository.getMonthlyTotals(user);
    }
    
    public BigDecimal getTotalExpenses(User user) {
        return expenseRepository.getTotalExpensesByUser(user);
    }
    
    public BigDecimal getMonthlyExpenses(User user) {
        return expenseRepository.getMonthlyExpensesByUser(user);
    }
    
    public BigDecimal getAvgDailyExpense(User user) {
        long days = 30; // Average over last 30 days
        BigDecimal total = expenseRepository.getMonthlyExpensesByUser(user);
        return total.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);
    }
    
    public String getHighestCategory(User user) {
        List<Map<String, Object>> categories = expenseRepository.getCategoryTotals(user);
        if (categories.isEmpty()) return "N/A";
        
        Map<String, Object> highest = categories.stream()
            .max((c1, c2) -> ((BigDecimal) c1.get("total")).compareTo((BigDecimal) c2.get("total")))
            .orElse(null);
            
        return highest != null ? highest.get("category").toString() : "N/A";
    }
    
    // Add this method to fix the error
    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }
}