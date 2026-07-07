package mymoney.controller;

import jakarta.validation.Valid;
import mymoney.dto.*;
import mymoney.model.Transaction;
import mymoney.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @GetMapping("/balance")
    public BigDecimal getBalance(){
        return service.getBalance();
    }

    @GetMapping("/monthly-transactions")
    public List<TransactionDTO> getMonthlyTransactions(){
        return service.getMonthlyTransactions();
    }

    @PostMapping("/income")
    public ResponseEntity<TransactionDTO> addIncome(@Valid @RequestBody IncomeRequest request){
        Transaction t = service.addIncome(request);
        TransactionDTO dto = service.toDTO(t);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(t.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(dto);
    }

    @PostMapping("/expense")
    public ResponseEntity<TransactionDTO> addExpense(@Valid @RequestBody ExpenseRequest request){
        Transaction t = service.addExpense(request);
        TransactionDTO dto = service.toDTO(t);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(t.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(dto);
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id){
        service.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories(){
        return service.getAllCategories();
    }

    @GetMapping("/transactions/month")
    public MonthExpenses getMonthExpenses(){
        return service.getMonthExpenses();
    }

}
