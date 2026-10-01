package bigu.eazybankbackend.controller;

import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/welcome")
//@CrossOrigin  //One of the many way to solve CORS issue on the server side...
public class WelcomeController {

    private final CompromisedPasswordChecker compromisedPasswordChecker;

    public WelcomeController(CompromisedPasswordChecker compromisedPasswordChecker) {
        this.compromisedPasswordChecker = compromisedPasswordChecker;
    }

    @GetMapping
    public String sayHi() {
        return "Welcome to Spring Application with security!";
    }

    @PostMapping("/is-compromised")
    public String checkPassword(@RequestBody PasswordRequest request) {

        CompromisedPasswordDecision decision =
                compromisedPasswordChecker.check(request.password());

        if (decision.isCompromised()) {
            throw new CompromisedPasswordException(
                    "The provided password is compromised and cannot be used."
            );
        }

        return "The provided password is not compromised";
    }

}
