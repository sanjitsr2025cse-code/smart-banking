package com.sanjit.banking.dto;

import jakarta.validation.constraints.NotBlank;

public class AccountUpdateRequest {
    @NotBlank(message = "Account type is required")
    private String accountType;

    public AccountUpdateRequest() {}
    public AccountUpdateRequest(String accountType) { this.accountType = accountType; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
}
