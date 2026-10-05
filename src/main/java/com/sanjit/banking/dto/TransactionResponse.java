package com.sanjit.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {
    private Long id;
    private String transactionType;
    private BigDecimal amount;
    private Long sourceAccountId;
    private Long destinationAccountId;
    private String description;
    private LocalDateTime createdAt;

    public TransactionResponse(Long id, String transactionType, BigDecimal amount,
                               Long sourceAccountId, Long destinationAccountId,
                               String description, LocalDateTime createdAt) {
        this.id = id;
        this.transactionType = transactionType;
        this.amount = amount;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.description = description;
        this.createdAt = createdAt;
    }
    public Long getId() { return id; }
    public String getTransactionType() { return transactionType; }
    public BigDecimal getAmount() { return amount; }
    public Long getSourceAccountId() { return sourceAccountId; }
    public Long getDestinationAccountId() { return destinationAccountId; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
