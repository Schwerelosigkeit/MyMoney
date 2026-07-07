package mymoney.dto;

import java.math.BigDecimal;

public class ExpenseRequest {

    private BigDecimal amount;
    private String category;
    private String description;

    public ExpenseRequest(){}

    public ExpenseRequest(BigDecimal amount, String category){
        this.amount = amount;
        this.category = category;
    }

    public ExpenseRequest(BigDecimal amount, String category, String description){
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
