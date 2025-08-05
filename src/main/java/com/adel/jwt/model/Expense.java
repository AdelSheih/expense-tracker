package com.adel.jwt.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    // Constructors
    public Expense() {}
    
    public Expense(String description, BigDecimal amount, LocalDate date, ExpenseCategory category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public ExpenseCategory getCategory() { return category; }
    public void setCategory(ExpenseCategory category) { this.category = category; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
}

