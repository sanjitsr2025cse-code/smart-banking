package com.sanjit.banking.repository;

import com.sanjit.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("select t from Transaction t where t.sourceAccount.id = :accountId or t.destinationAccount.id = :accountId order by t.createdAt desc")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId);
}
