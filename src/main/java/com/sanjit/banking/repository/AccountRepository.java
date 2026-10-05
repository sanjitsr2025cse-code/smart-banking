package com.sanjit.banking.repository;

import com.sanjit.banking.entity.Account;
import com.sanjit.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user);
}
