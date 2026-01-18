package mymoney.entity;

import java.time.LocalDateTime;

public class Transaction {

    private Long id;
    private double amount;
    private TransactionType type;
    private ExpenseCategories category;
    private LocalDateTime date;
    private String description;

    public Transaction(){}

    public Transaction(double amount, TransactionType type, ExpenseCategories category, LocalDateTime date){
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }

    public Transaction(double amount, TransactionType type, ExpenseCategories category, LocalDateTime date, String description){
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.category = category;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public ExpenseCategories getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategories category) {
        this.category = category;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
