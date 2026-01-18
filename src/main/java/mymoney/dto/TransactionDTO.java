package mymoney.dto;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;
    private double amount;
    private String type;
    private String category;
    private String color;
    private LocalDateTime date;
    private String description;


    public TransactionDTO(Long id, double amount, String type, String category, String color, LocalDateTime date, String description) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.color = color;
        this.date = date;
        this.description = description;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
