package com.sanjit.banking.dto;

import jakarta.validation.constraints.NotBlank;

public class AccountRequest {
    @NotBlank(message = "Account type is required")
    private String accountType;

    public AccountRequest() {}
    public AccountRequest(String accountType) { this.accountType = accountType; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
}
