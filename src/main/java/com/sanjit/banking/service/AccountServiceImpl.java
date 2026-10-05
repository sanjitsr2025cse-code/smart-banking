package com.sanjit.banking.service;

import com.sanjit.banking.dto.AccountRequest;
import com.sanjit.banking.dto.AccountResponse;
import com.sanjit.banking.dto.AccountUpdateRequest;
import com.sanjit.banking.entity.Account;
import com.sanjit.banking.entity.User;
import com.sanjit.banking.exception.ResourceNotFoundException;
import com.sanjit.banking.repository.AccountRepository;
import com.sanjit.banking.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class











































AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AccountResponse createAccount(AccountRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);

        return toResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse getAccountById(Long id) {
        return toResponse(findAccount(id));
    }

    @Override
    public List<AccountResponse> getAccountsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return accountRepository.findByUser(user).stream().map(this::toResponse).toList();
    }

    @Override
    public AccountResponse updateAccount(Long id, AccountUpdateRequest request) {
        Account account = findAccount(id);
        account.setAccountType(request.getAccountType());
        return toResponse(accountRepository.save(account));
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = findAccount(id);
        accountRepository.delete(account);
    }

    private Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(account.getId(), account.getAccountNumber(), account.getAccountType(),
                account.getBalance(), account.getUser().getId(), account.getCreatedAt(), account.getUpdatedAt());
    }
}
