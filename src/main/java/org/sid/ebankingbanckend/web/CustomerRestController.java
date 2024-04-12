package org.sid.ebankingbanckend.web;

import lombok.AllArgsConstructor;
import org.sid.ebankingbanckend.dtos.CustomerDTO;
import org.sid.ebankingbanckend.entities.Customer;
import org.sid.ebankingbanckend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbanckend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomer();
    }

    @GetMapping("/customers/{id}")
    public  CustomerDTO getCustomer( @PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
          return bankAccountService.getCustomer(customerId);
    }
    // ajouter un client
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
       return bankAccountService.saveCustomer(customerDTO);

    }

}
