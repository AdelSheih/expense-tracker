package com.adel.jwt.controller;

import com.adel.jwt.model.Expense;
import com.adel.jwt.model.ExpenseCategory;
import com.adel.jwt.model.User;
import com.adel.jwt.service.ExpenseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }
    
    @GetMapping
    public String listExpenses(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("expenses", expenseService.getAllExpensesByUser(user));
        return "expenses";
    }
    
    @GetMapping("/new")
    public String showExpenseForm(Model model) {
        model.addAttribute("expense", new Expense());
        model.addAttribute("categories", ExpenseCategory.values());
        return "expense-form";
    }
    
    @PostMapping
    public String saveExpense(@ModelAttribute Expense expense, 
                             @AuthenticationPrincipal User user) {
        expense.setUser(user);
        expenseService.saveExpense(expense);
        return "redirect:/expenses";
    }
    
    @GetMapping("/edit/{id}")
    public String editExpense(@PathVariable Long id, 
                             @AuthenticationPrincipal User user,
                             Model model) {
        Expense expense = expenseService.getExpenseById(id);
        
        // Check if expense belongs to current user
        if (!expense.getUser().getId().equals(user.getId())) {
            return "redirect:/expenses";
        }
        
        model.addAttribute("expense", expense);
        model.addAttribute("categories", ExpenseCategory.values());
        return "expense-form";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id, 
                               @AuthenticationPrincipal User user) {
        Expense expense = expenseService.getExpenseById(id);
        
        // Check if expense belongs to current user
        if (expense.getUser().getId().equals(user.getId())) {
            expenseService.deleteExpense(id);
        }
        
        return "redirect:/expenses";
    }
}