package com.sanjit.banking.controller;

import com.sanjit.banking.dto.MoneyRequest;
import com.sanjit.banking.dto.TransactionResponse;
import com.sanjit.banking.dto.TransferRequest;
import com.sanjit.banking.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<TransactionResponse> deposit(@PathVariable Long accountId,
                                                       @Valid @RequestBody MoneyRequest request) {
        return ResponseEntity.ok(transactionService.deposit(accountId, request));
    }

    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<TransactionResponse> withdraw(@PathVariable Long accountId,
                                                        @Valid @RequestBody MoneyRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(accountId, request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transfer(request));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getAccountTransactions(accountId));
    }
}
