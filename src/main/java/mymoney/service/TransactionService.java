package mymoney.service;

import mymoney.dto.*;
import mymoney.model.ExpenseCategory;
import mymoney.model.Transaction;
import mymoney.model.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final List<Transaction> transactions = new ArrayList<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);

    private static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000.00");
    private static final int MAX_DESCRIPTION_LENGTH = 500;
    private static final Pattern DESCRIPTION_PATTERN =
            Pattern.compile("^[a-zA-Zа-яА-Я0-9\\s.,?!-]*$");

    public BigDecimal getBalance() {
        return transactions.stream()
                .map(t -> t.getType() == TransactionType.INCOME ? t.getAmount() : t.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<TransactionDTO> getMonthlyTransactions() {
        YearMonth currentMonth = YearMonth.now();

        return transactions.stream()
                .filter(t -> t.getDate().getYear() == currentMonth.getYear()
                        && t.getDate().getMonth() == currentMonth.getMonth())
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .map(this::toDTO)
                .toList();
    }

    public Transaction addIncome(IncomeRequest request) {
        validateAmount(request.getAmount());
        validateDescription(request.getDescription());

        Transaction transaction = new Transaction(
                normalizeAmount(request.getAmount()),
                TransactionType.INCOME,
                null,
                LocalDateTime.now(),
                normalizeDescription(request.getDescription())
        );
        transaction.setId(idGenerator.getAndIncrement());
        transactions.add(transaction);
        return transaction;
    }

    public Transaction addExpense(ExpenseRequest request) {
        validateAmount(request.getAmount());
        validateDescription(request.getDescription());
        validateCategory(request.getCategory());

        ExpenseCategory category;
        try {
            category = ExpenseCategory.valueOf(request.getCategory());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid expense category.");
        }

        Transaction transaction = new Transaction(
                normalizeAmount(request.getAmount()),
                TransactionType.EXPENSE,
                category,
                LocalDateTime.now(),
                normalizeDescription(request.getDescription())
        );
        transaction.setId(idGenerator.getAndIncrement());
        transactions.add(transaction);
        return transaction;
    }

    public void deleteTransaction(Long id) {
        boolean removed = transactions.removeIf(t -> t.getId().equals(id));
        if (!removed) {
            throw new IllegalArgumentException("Transaction with id=" + id + " was not found.");
        }
    }

    public List<CategoryDTO> getAllCategories() {
        return Arrays.stream(ExpenseCategory.values())
                .map(c -> new CategoryDTO(c.name(), c.getName(), c.getColor()))
                .collect(Collectors.toList());
    }

    public TransactionDTO toDTO(Transaction transaction) {
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

    public MonthExpenses getMonthExpenses() {
        YearMonth currentMonth = YearMonth.now();

        List<Transaction> monthlyExpenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE
                        && t.getDate().getYear() == currentMonth.getYear()
                        && t.getDate().getMonth() == currentMonth.getMonth())
                .toList();

        BigDecimal totalExpense = monthlyExpenses.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<ExpenseCategory, BigDecimal> expensesByCategory = monthlyExpenses.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.mapping(
                                Transaction::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        Map<ExpenseCategory, BigDecimal> percentages = new HashMap<>();
        if (totalExpense.compareTo(BigDecimal.ZERO) > 0) {
            for (Map.Entry<ExpenseCategory, BigDecimal> entry : expensesByCategory.entrySet()) {
                BigDecimal percent = entry.getValue()
                        .multiply(new BigDecimal("100"))
                        .divide(totalExpense, 2, RoundingMode.HALF_UP);
                percentages.put(entry.getKey(), percent);
            }
        } else {
            for (ExpenseCategory key : expensesByCategory.keySet()) {
                percentages.put(key, BigDecimal.ZERO);
            }
        }

        return new MonthExpenses(totalExpense, expensesByCategory, percentages);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive (minimum 0.01).");
        }

        if (amount.compareTo(MAX_AMOUNT) > 0) {
            throw new IllegalArgumentException("Amount must not exceed 1,000,000.");
        }

        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount must have no more than 2 decimal places.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            return;
        }

        String trimmed = description.trim();

        // пока только это добавила, нужно проверку postman
        if (trimmed.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException(
                    "Comment must not exceed 500 characters."
            );
        }

        if (!DESCRIPTION_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(
                    "Comment may only contain letters, numbers, spaces, and basic punctuation (,.?!-)."
            );
        }
    }

    private void validateCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category is required.");
        }
    }

    private String normalizeDescription(String description) {
        if (description == null) {
            return null;
        }

        String trimmed = description.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private BigDecimal normalizeAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.UNNECESSARY);
    }

}
