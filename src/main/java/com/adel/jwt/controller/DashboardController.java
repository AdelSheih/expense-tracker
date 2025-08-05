package com.adel.jwt.controller;

import com.adel.jwt.model.User;
import com.adel.jwt.service.ExpenseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DashboardController {
    private final ExpenseService expenseService;
    private final ObjectMapper objectMapper;
    public DashboardController(ExpenseService expenseService) {
        this.expenseService = expenseService;
        this.objectMapper = new ObjectMapper();
    }
    @GetMapping("/logout")
    public String logout(Authentication authentication) {
        authentication.setAuthenticated(false);
        return "redirect:/login";  // Redirect to login page
        // Additional logout logic if needed

    }
    
    @GetMapping("/dashboard")
    public String showDashboard(Authentication authentication, 
                           Model model) {
        try {
            // Convert data to JSON for Chart.js
            User user = (User) authentication.getPrincipal();
            model.addAttribute("categoryData", 
                objectMapper.writeValueAsString(expenseService.getCategoryTotals(user)));
            model.addAttribute("monthlyData", 
                objectMapper.writeValueAsString(expenseService.getMonthlyTotals(user)));
        } catch (JsonProcessingException e) {
            // Fallback to empty arrays
            model.addAttribute("categoryData", "[]");
            model.addAttribute("monthlyData", "[]");
        }

        User user = (User) authentication.getPrincipal();
        // Add dashboard metrics
        model.addAttribute("recentExpenses", expenseService.getRecentExpenses(user));
        model.addAttribute("totalExpenses", expenseService.getTotalExpenses(user));
        model.addAttribute("monthlyExpenses", expenseService.getMonthlyExpenses(user));
        model.addAttribute("avgDaily", expenseService.getAvgDailyExpense(user));
        model.addAttribute("highestCategory", expenseService.getHighestCategory(user));
        
        return "dashboard";
    }
}