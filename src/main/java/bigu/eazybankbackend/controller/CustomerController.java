package bigu.eazybankbackend.controller;

import bigu.eazybankbackend.model.Customer;
import bigu.eazybankbackend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController { ///api/v1/customers/register

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Customer>> getCustomers(){
        var customers = customerService.findAll();
        if(customers.isEmpty()) return ResponseEntity.noContent().build();
        return  ResponseEntity.ok().body(customers);
    }

    @PostMapping(path = "/register",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setCreateDt(LocalDate.now());
            customer.setPwd(hashPwd);
            Customer savedCustomer = customerService.register(customer);

            if(savedCustomer.getId()>0) {
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("Costumer created successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("Customer registration failed");
            }
        }catch (Exception ex) {
            log.error("Error during customer registration", ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal error occurred.");
        }

    }

    @GetMapping("/user")
    public Customer getUserDetailsAfterLogin(Authentication authentication) {
        return customerService.findByEmail(authentication.getName());
    }
}
