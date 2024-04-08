package org.sid.ebankingbanckend.repositories;

import org.sid.ebankingbanckend.entities.AccountOperation;
import org.sid.ebankingbanckend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
