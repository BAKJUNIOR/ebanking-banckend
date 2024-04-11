package org.sid.ebankingbanckend.web;

import lombok.AllArgsConstructor;
import org.sid.ebankingbanckend.entities.Customer;
import org.sid.ebankingbanckend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customer")
    public List<Customer> customers(){
        return bankAccountService.listCustomer();
    }
}
