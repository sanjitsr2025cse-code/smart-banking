package com.sanjit.banking.service;

import com.sanjit.banking.dto.MoneyRequest;
import com.sanjit.banking.dto.TransactionResponse;
import com.sanjit.banking.dto.TransferRequest;
import java.util.List;

public interface TransactionService {
    TransactionResponse deposit(Long accountId, MoneyRequest request);
    TransactionResponse withdraw(Long accountId, MoneyRequest request);
    TransactionResponse transfer(TransferRequest request);
    List<TransactionResponse> getAccountTransactions(Long accountId);
}
