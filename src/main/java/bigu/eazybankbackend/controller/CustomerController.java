package bigu.eazybankbackend.controller;

import bigu.eazybankbackend.config.JWTSecretKey;
import bigu.eazybankbackend.constants.ApplicationConstants;
import bigu.eazybankbackend.model.Customer;
import bigu.eazybankbackend.model.LoginRequestDTO;
import bigu.eazybankbackend.model.LoginResponseDTO;
import bigu.eazybankbackend.service.CustomerService;
import ch.qos.logback.core.util.StringCollectionUtil;
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
import org.springframework.util.StringUtils;
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
    private final PasswordEncoder passwordEncoder;
    private final Environment env;
    private final AuthenticationManager authenticationManager;

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


    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponseDTO> apiLogin (@RequestBody LoginRequestDTO loginRequest) {
        String jwt = "";
        Instant now = Instant.now();
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(),
                loginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);
        if(null != authenticationResponse && authenticationResponse.isAuthenticated()) {
            if (null != env) {
                SecretKey secretKey = JWTSecretKey.getSKey(env.getRequiredProperty(ApplicationConstants.JWT_SECRET));
                 jwt = Jwts.builder()
                        .issuer("https://auth.eazybank.com")
                        .subject(authenticationResponse.getName())
                        .claim("authorities",
                                authenticationResponse.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList())
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(now.plus(5, ChronoUnit.MINUTES)))
                        .signWith(secretKey)
                        .compact();
            }
        }

        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER,jwt)
                .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
    }
}
