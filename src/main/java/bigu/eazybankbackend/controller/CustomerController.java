package bigu.eazybankbackend.controller;

import bigu.eazybankbackend.model.Customer;
import bigu.eazybankbackend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Customer>> getCustomers(){
        var customers = customerService.findAll();
        if(customers.isEmpty()) return ResponseEntity.noContent().build();
        return  ResponseEntity.ok().body(customers);
    }

    @PostMapping(params = "/register",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            Customer savedCustomer = customerService.register(customer);

            if(savedCustomer.getId()>0) {
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("Costumer created successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("Customer registration failed");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("An exception occurred during the customer registration: " + ex.getMessage());
        }

    }
}
