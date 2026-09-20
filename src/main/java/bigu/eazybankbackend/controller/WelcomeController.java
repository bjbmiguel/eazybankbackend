package bigu.eazybankbackend.controller;

import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/welcome")

public class WelcomeController {

    private final CompromisedPasswordChecker compromisedPasswordChecker;

    public WelcomeController(CompromisedPasswordChecker compromisedPasswordChecker) {
        this.compromisedPasswordChecker = compromisedPasswordChecker;
    }

    @GetMapping
    public String sayHi(){
        return "Welcome to Spring Application with security!";
    }

    @GetMapping("/is-compromised/{password}")
    public String checkPassword(@PathVariable String password) {

        CompromisedPasswordDecision decision =
                compromisedPasswordChecker.check(password);

        if (decision.isCompromised()) {
            throw new CompromisedPasswordException(
                    "The provided password is compromised and cannot be used."
            );
        }

        return "The provided password is not compromised";
    }
}
