package mymoney.service;

import mymoney.dto.*;
import mymoney.entity.ExpenseCategories;
import mymoney.entity.Transaction;
import mymoney.entity.TransactionType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final List<Transaction> transactions = new ArrayList<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);

    public double getBalance(){
        return transactions.stream()
                .mapToDouble(t -> t.getType() == TransactionType.INCOME ? t.getAmount() : -t.getAmount())
                .sum();
    }

    public List<TransactionDTO> getMonthlyTransactions() {
        YearMonth currentMonth = YearMonth.now();

        return transactions.stream()
                .filter(t -> t.getDate().getYear() == currentMonth.getYear()
                        && t.getDate().getMonth() == currentMonth.getMonth())
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .map(this::toDTO).toList();
    }

    public Transaction addIncome(IncomeRequest request){
        Transaction t = new Transaction(request.getAmount(), TransactionType.INCOME, null, LocalDateTime.now(), request.getDescription());
        t.setId(idGenerator.incrementAndGet());
        transactions.add(t);
        return t;
    }

    public Transaction addExpense(ExpenseRequest request){
        ExpenseCategories category = ExpenseCategories.valueOf(request.getCategory());
        Transaction t = new Transaction(request.getAmount(), TransactionType.EXPENSE, category, LocalDateTime.now(), request.getDescription());
        t.setId(idGenerator.incrementAndGet());
        transactions.add(t);
        return t;
    }

    public void deleteTransaction(Long id){
        transactions.removeIf(t -> t.getId().equals(id));
    }

    public List<CategoryDTO> getAllCategories(){
        return Arrays.stream(ExpenseCategories.values())
                .map(c -> new CategoryDTO(c.name(), c.getName(), c.getColor()))
                .collect(Collectors.toList());
    }

    public TransactionDTO toDTO(Transaction transaction){
        return new TransactionDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType().name(),
                transaction.getCategory() != null ? transaction.getCategory().getName() : null,
                transaction.getCategory() != null ? transaction.getCategory().getColor() : null,
                transaction.getDate(),
                transaction.getDescription()
        );
    }

    public MonthExpenses getMonthExpenses(){
        YearMonth currentMonth = YearMonth.now();

        List<Transaction> monthlyExpenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE
                        && t.getDate().getYear() == currentMonth.getYear()
                        && t.getDate().getMonth() == currentMonth.getMonth())
                .toList();

        double totalExpense = monthlyExpenses.stream().mapToDouble(Transaction::getAmount).sum();

        Map<ExpenseCategories, Double> expensesByCategory = monthlyExpenses.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));

        Map<ExpenseCategories, Double> percentages = expensesByCategory.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> (e.getValue() / totalExpense) * 100
                ));

        return new MonthExpenses(totalExpense, expensesByCategory, percentages);
    }

}
