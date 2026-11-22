package bigu.eazybankbackend.controller;

import bigu.eazybankbackend.constants.ApplicationConstants;
import bigu.eazybankbackend.model.Customer;
import bigu.eazybankbackend.model.LoginRequestDTO;
import bigu.eazybankbackend.model.LoginResponseDTO;
import bigu.eazybankbackend.service.CustomerService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController { ///api/v1/customers/register

    private final CustomerService customerService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Customer>> getCustomers(){
        var customers = customerService.findAll();
        if(customers.isEmpty()) return ResponseEntity.noContent().build();
        return  ResponseEntity.ok().body(customers);
    }


    @GetMapping("/user")
    public Customer getUserDetailsAfterLogin(Authentication authentication) {
        return customerService.findByEmail(authentication.getName());
    }



}
