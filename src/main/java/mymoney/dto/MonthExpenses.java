package mymoney.dto;

import mymoney.model.ExpenseCategory;

import java.math.BigDecimal;
import java.util.Map;

public class MonthExpenses {

    private BigDecimal totalExpenses;
    private Map<ExpenseCategory, BigDecimal> expensesByCategory;
    private Map<ExpenseCategory, BigDecimal> percentages;

    public MonthExpenses(BigDecimal totalExpenses, Map<ExpenseCategory, BigDecimal> expensesByCategory, Map<ExpenseCategory, BigDecimal> percentages) {
        this.totalExpenses = totalExpenses;
        this.expensesByCategory = expensesByCategory;
        this.percentages = percentages;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Map<ExpenseCategory, BigDecimal> getExpensesByCategory() {
        return expensesByCategory;
    }

    public void setExpensesByCategory(Map<ExpenseCategory, BigDecimal> expensesByCategory) {
        this.expensesByCategory = expensesByCategory;
    }

    public Map<ExpenseCategory, BigDecimal> getPercentages() {
        return percentages;
    }

    public void setPercentages(Map<ExpenseCategory, BigDecimal> percentages) {
        this.percentages = percentages;
    }

}
