package com.sanjit.banking.service;

import com.sanjit.banking.dto.AccountRequest;
import com.sanjit.banking.dto.AccountResponse;
import com.sanjit.banking.dto.AccountUpdateRequest;
import java.util.List;

public interface AccountService {
    AccountResponse createAccount(AccountRequest request, Long userId);
    AccountResponse getAccountById(Long id);
    List<AccountResponse> getAccountsByUser(Long userId);
    AccountResponse updateAccount(Long id, AccountUpdateRequest request);
    void deleteAccount(Long id);
}
