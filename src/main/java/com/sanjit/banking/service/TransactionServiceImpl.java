package com.sanjit.banking.service;

import com.sanjit.banking.dto.MoneyRequest;
import com.sanjit.banking.dto.TransactionResponse;
import com.sanjit.banking.dto.TransferRequest;
import com.sanjit.banking.entity.Account;
import com.sanjit.banking.entity.Transaction;
import com.sanjit.banking.exception.ResourceNotFoundException;
import com.sanjit.banking.repository.AccountRepository;
import com.sanjit.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository,
                                  TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public TransactionResponse deposit(Long accountId, MoneyRequest request) {
        Account account = findAccount(accountId);
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(request.getAmount());
        transaction.setDestinationAccount(account);
        transaction.setDescription(request.getDescription());

        return toResponse(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(Long accountId, MoneyRequest request) {
        Account account = findAccount(accountId);
        ensureSufficientBalance(account, request.getAmount());
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setTransactionType("WITHDRAW");
        transaction.setAmount(request.getAmount());
        transaction.setSourceAccount(account);
        transaction.setDescription(request.getDescription());

        return toResponse(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        if (request.getSourceAccountId().equals(request.getDestinationAccountId())) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }

        Account source = findAccount(request.getSourceAccountId());
        Account destination = findAccount(request.getDestinationAccountId());
        ensureSufficientBalance(source, request.getAmount());

        source.setBalance(source.getBalance().subtract(request.getAmount()));
        destination.setBalance(destination.getBalance().add(request.getAmount()));
        accountRepository.save(source);
        accountRepository.save(destination);

        Transaction transaction = new Transaction();
        transaction.setTransactionType("TRANSFER");
        transaction.setAmount(request.getAmount());
        transaction.setSourceAccount(source);
        transaction.setDestinationAccount(destination);
        transaction.setDescription(request.getDescription());

        return toResponse(transactionRepository.save(transaction));
    }

    @Override
    public List<TransactionResponse> getAccountTransactions(Long accountId) {
        findAccount(accountId);
        return transactionRepository.findByAccountId(accountId).stream().map(this::toResponse).toList();
    }

    private Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    private void ensureSufficientBalance(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(transaction.getId(), transaction.getTransactionType(), transaction.getAmount(),
                transaction.getSourceAccount() == null ? null : transaction.getSourceAccount().getId(),
                transaction.getDestinationAccount() == null ? null : transaction.getDestinationAccount().getId(),
                transaction.getDescription(), transaction.getCreatedAt());
    }
}
