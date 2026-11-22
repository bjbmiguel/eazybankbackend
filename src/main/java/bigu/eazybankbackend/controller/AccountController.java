package bigu.eazybankbackend.controller;


import bigu.eazybankbackend.model.Accounts;
import bigu.eazybankbackend.repository.AccountsRepository;
import bigu.eazybankbackend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountsRepository accountsRepository;
    private final CustomerService customerService;

    @GetMapping("/myAccount")
    public Accounts getAccountDetails(@RequestParam String email) {
        var customer = customerService.findByEmail(email);
        if(Objects.nonNull(customer)){
            Accounts accounts = accountsRepository.findByCustomerId(customer.getId());
            if (accounts != null) {
                return accounts;
            } else {
                return null;
            }
        }

        return null;
    }

}
