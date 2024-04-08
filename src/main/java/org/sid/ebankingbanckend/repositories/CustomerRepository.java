package org.sid.ebankingbanckend.repositories;

import org.sid.ebankingbanckend.entities.BankAccount;
import org.sid.ebankingbanckend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
