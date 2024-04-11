package org.sid.ebankingbanckend.services;

import org.sid.ebankingbanckend.entities.BankAccount;
import org.sid.ebankingbanckend.entities.CurrentAccount;
import org.sid.ebankingbanckend.entities.Customer;
import org.sid.ebankingbanckend.entities.SavingAccount;
import org.sid.ebankingbanckend.exceptions.BalanceNotSufficentException;
import org.sid.ebankingbanckend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbanckend.exceptions.CustomerNotFoundException;

import java.util.List;

//Ici, on va define les besoins fonctionnels d'appli, define toutes les opérations
public interface BankAccountService {
    // operation pour créer ou ajouter des clients
    Customer saveCustomer(Customer customer);   // créer une méthode saveCustomer

    // operation pour créer un compte
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    // operation pour consulter une liste de client
    List<Customer> listCustomer();

    // operation pour consulter un compte
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;

    // operation pour debit et crédit

    void debit(String accoundId, double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accoundId, double amount,String description) throws BankAccountNotFoundException;
    void transfert(String accoundIdSource, String accoundIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;

    List<BankAccount> bankAccountList();
}
