package mymoney.component;

import mymoney.dto.ExpenseRequest;
import mymoney.dto.IncomeRequest;
import mymoney.service.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TransactionService transactionService;

    public DataInitializer(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void run(String... args) {

        transactionService.addIncome(new IncomeRequest(
                new BigDecimal("15000.00")
        ));

        transactionService.addExpense(new ExpenseRequest(
                new BigDecimal("500.00"),
                "OTHER"
        ));

        transactionService.addExpense(new ExpenseRequest(
                new BigDecimal("1300.00"),
                "MEDICINE"
        ));

        transactionService.addExpense(new ExpenseRequest(
                new BigDecimal("1100.00"),
                "EDUCATION"
        ));

        transactionService.addExpense(new ExpenseRequest(
                new BigDecimal("600.00"),
                "SPORT"
        ));

        transactionService.addExpense(new ExpenseRequest(
                new BigDecimal("1500.00"),
                "FOODSTUFF",
                "Initial groceries"
        ));

        transactionService.addExpense(new ExpenseRequest(
                new BigDecimal("800.00"),
                "TRANSPORT",
                "Initial transport costs"
        ));

        transactionService.addExpense(new ExpenseRequest(
                new BigDecimal("1200.00"),
                "RESTAURANTS",
                "Initial cafes and restaurants"
        ));

        transactionService.addExpense(new ExpenseRequest(
                new BigDecimal("1000.00"),
                "ENTERTAINMENT",
                "Initial entertainment"
        ));

        transactionService.addExpense(new ExpenseRequest(
                new BigDecimal("900.00"),
                "HOUSE",
                "Initial home goods"
        ));

        transactionService.addExpense(new ExpenseRequest(
                new BigDecimal("700.00"),
                "UTILITIES",
                "Initial utilities payment"
        ));

        transactionService.addIncome(new IncomeRequest(
                new BigDecimal("50000.00"),
                "Initial salary"
        ));

    }
}