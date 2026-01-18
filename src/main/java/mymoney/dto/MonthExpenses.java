package mymoney.dto;

import mymoney.entity.ExpenseCategories;

import java.util.Map;

public class MonthExpenses {

    private double totalExpenses;
    private Map<ExpenseCategories, Double> expensesByCategory;
    private Map<ExpenseCategories, Double> percentages;

    public MonthExpenses(double totalExpenses, Map<ExpenseCategories, Double> expensesByCategory, Map<ExpenseCategories, Double> percentages) {
        this.totalExpenses = totalExpenses;
        this.expensesByCategory = expensesByCategory;
        this.percentages = percentages;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Map<ExpenseCategories, Double> getExpensesByCategory() {
        return expensesByCategory;
    }

    public void setExpensesByCategory(Map<ExpenseCategories, Double> expensesByCategory) {
        this.expensesByCategory = expensesByCategory;
    }

    public Map<ExpenseCategories, Double> getPercentages() {
        return percentages;
    }

    public void setPercentages(Map<ExpenseCategories, Double> percentages) {
        this.percentages = percentages;
    }

}
