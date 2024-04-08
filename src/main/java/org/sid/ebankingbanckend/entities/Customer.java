package org.sid.ebankingbanckend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
//    Un client (Customer) peut avoir plusieurs comptes
    @OneToMany(mappedBy = "customer")
    private List<BankAccount> bankAccounts;
}
