package org.sid.ebankingbanckend.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbanckend.dtos.CustomerDTO;
import org.sid.ebankingbanckend.entities.*;
import org.sid.ebankingbanckend.enums.OperationType;
import org.sid.ebankingbanckend.exceptions.BalanceNotSufficentException;
import org.sid.ebankingbanckend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbanckend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbanckend.mappers.BankAccountMapperImpl;
import org.sid.ebankingbanckend.repositories.AccountOperationRepository;
import org.sid.ebankingbanckend.repositories.BankAccountRepository;
import org.sid.ebankingbanckend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Data
@AllArgsConstructor
@Slf4j // pour log les messages
public class BankAccountServiceImpl implements BankAccountService{

   private CustomerRepository customerRepository;
   private BankAccountRepository bankAccountRepository;
   private AccountOperationRepository accountOperationRepository;
   private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Client enregistrer avec success !!");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        //Vérifier si le client existe avant de créer un compte
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);

        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return savedBankAccount;

    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        //Vérifier si le client existe avant de créer un compte
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);

        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return savedBankAccount;

    }


    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customersDTOS = customers.stream()
                .map(customer->dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        return customersDTOS;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        //Vérifier si le compte existe avant de le consulter
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accoundId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount = getBankAccount(accoundId); // récupérer le compte

        // pour operation de retrait debit si le montant du compte < au montant demandé génère une exception
        if (bankAccount.getBalance()< amount)
            throw new BalanceNotSufficentException(" Votre solde est insuffisant ");

        // Enregistrer l'opération
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        // mettre à jour le solde apres opération de débit
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);


    }

    @Override
    public void credit(String accoundId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accoundId); // récupérer le compte

        // Enregistrer l'opération
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        // mettre à jour le solde apres opération de débit
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfert(String accoundIdSource, String accoundIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accoundIdSource , amount , "Transfert vers"+accoundIdDestination);
        credit(accoundIdDestination, amount, "Transfert from"+accoundIdSource);

    }
    @Override
    public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    }

    // permet de consulter un client

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
      Customer customer =  customerRepository.findById(customerId).orElseThrow(()->new CustomerNotFoundException("Customer n'existe pas"));

        return dtoMapper.fromCustomer(customer);

    }
}
