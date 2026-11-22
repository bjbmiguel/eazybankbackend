package bigu.eazybankbackend.controller;


import bigu.eazybankbackend.model.Loans;
import bigu.eazybankbackend.repository.LoanRepository;
import bigu.eazybankbackend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class LoansController {

    private final LoanRepository loanRepository;
    private final CustomerService customerService;

    @GetMapping("/myLoans")
    public List<Loans> getLoanDetails(@RequestParam String email) {
        var customer = customerService.findByEmail(email);
        if(!Objects.nonNull(customer)) return null;
        List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(customer.getId());
        if (loans != null) {
            return loans;
        } else {
            return null;
        }
    }

}
