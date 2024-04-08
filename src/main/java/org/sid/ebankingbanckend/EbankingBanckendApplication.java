package org.sid.ebankingbanckend;

import org.sid.ebankingbanckend.entities.AccountOperation;
import org.sid.ebankingbanckend.entities.CurrentAccount;
import org.sid.ebankingbanckend.entities.Customer;
import org.sid.ebankingbanckend.entities.SavingAccount;
import org.sid.ebankingbanckend.enums.AccountStatus;
import org.sid.ebankingbanckend.enums.OperationType;
import org.sid.ebankingbanckend.repositories.AccountOperationRepository;
import org.sid.ebankingbanckend.repositories.BankAccountRepository;
import org.sid.ebankingbanckend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBanckendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBanckendApplication.class, args);
    }

    // Enregistrer les données dans les tables
    @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
                //crée 3 clients
            Stream.of("Delmas" , "Yassine" , "Aiche").forEach(name-> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);

            });

            //  Pour chaque client (customer), je vais enregistrer deux comptes un compte épargne(SavingAccount) et courant(CurrentAccount)
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());// générer un String aléatoire unique pour Id
                currentAccount.setBalance(Math.random()*9000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);


                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());// générer un String aléatoire unique pour Id
                savingAccount.setBalance(Math.random()*9000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });

           bankAccountRepository.findAll().forEach(acc->{

               for ( int i =0 ; i<5 ; i++){
                   AccountOperation accountOperation = new AccountOperation();
                   accountOperation.setOperationDate(new Date());
                   accountOperation.setAmount(Math.random()*12000);
                   accountOperation.setType(Math.random() > 0.5? OperationType.DEBIT: OperationType.CREDIT);
                   accountOperation.setBankAccount(acc);
                   accountOperationRepository.save(accountOperation);
               }
           });

        };

    }

}
