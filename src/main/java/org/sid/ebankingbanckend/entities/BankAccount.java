package org.sid.ebankingbanckend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankingbanckend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // pour héritage dans la classe mere
// comme on utilise SINGLE_TABLE, on crée le type
@DiscriminatorColumn(name = "TYPE", length = 4)
@Entity
@Data  @NoArgsConstructor  @AllArgsConstructor
public class  BankAccount {
    @Id
    private String id; //le RIP
    private double balance; // le solde
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    // Un compte appartient à un client
    @ManyToOne
    private Customer customer;
    // Un compte peut effectuer plusieurs opérations
    @OneToMany(mappedBy = "bankAccount" , fetch = FetchType.LAZY)
    private List<AccountOperation> accountOperations;
}
