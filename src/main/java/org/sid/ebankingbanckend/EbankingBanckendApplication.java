package org.sid.ebankingbanckend;

import org.sid.ebankingbanckend.entities.*;
import org.sid.ebankingbanckend.enums.AccountStatus;
import org.sid.ebankingbanckend.enums.OperationType;
import org.sid.ebankingbanckend.exceptions.BalanceNotSufficentException;
import org.sid.ebankingbanckend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbanckend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbanckend.repositories.AccountOperationRepository;
import org.sid.ebankingbanckend.repositories.BankAccountRepository;
import org.sid.ebankingbanckend.repositories.CustomerRepository;
import org.sid.ebankingbanckend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBanckendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBanckendApplication.class, args);
    }
    @Bean
            CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Hassan" , "Yassine" , "Mohamed").forEach(name-> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
     // crée pour chaque client un compte épargne et courant
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*9000,9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId() );
       // Pour chaque compte, on crée une dizaine d'opérations
                    List<BankAccount> bankAccounts = bankAccountService.bankAccountList();

                    for (BankAccount bankAccount:bankAccounts) {
                        for (int i = 0; i < 10; i++) {
                            bankAccountService.credit(bankAccount.getId(), 10000+Math.random()*12000, "Crédit");
                            bankAccountService.debit(bankAccount.getId(), 1000+Math.random()*9000, "Débit");

                        }
                    }

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFoundException  | BalanceNotSufficentException e ) {
                    e.printStackTrace();
                }
            });
        };
    }

    // Enregistrer les données dans les tables
   //@Bean
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

            //Pour chaque compte, on crée une dizaine d'opérations
           bankAccountRepository.findAll().forEach(acc->{


               for ( int i =0 ; i<5 ; i++){
                   AccountOperation accountOperation = new AccountOperation();
                   accountOperation.setOperationDate(new Date());
                   accountOperation.setAmount(Math.random()*12000);
                   accountOperation.setType(Math.random() > 0.5? OperationType.DEBIT: OperationType.CREDIT);
                   accountOperation.setBankAccount(acc);
                   accountOperation.setDescription("description de l'opération");
                   accountOperationRepository.save(accountOperation);
               }
           });

        };

    }

}
