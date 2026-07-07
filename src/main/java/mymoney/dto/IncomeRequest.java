package mymoney.dto;

import java.math.BigDecimal;

public class IncomeRequest {

    private BigDecimal amount;
    private String description;

    public IncomeRequest(){}

    public IncomeRequest(BigDecimal amount) {
        this.amount = amount;
    }

    public IncomeRequest(BigDecimal amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

}
