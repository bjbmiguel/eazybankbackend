package bigu.eazybankbackend.config;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Profile("prd")
@Component
@RequiredArgsConstructor //Gera um construtor apenas para os campos obrigatórios, ou seja:campos final e anotados como @NonNull
//@AllArgsConstructor //Gera um construtor com todos os campos da classe, independentemente de serem final ou não
public class EazyBankProdUserPasswordAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable  Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String rawPwd = Objects.requireNonNull(authentication.getCredentials()).toString();
        var user = userDetailsService.loadUserByUsername(userName);
        if(passwordEncoder.matches(rawPwd, user.getPassword())){
            return new UsernamePasswordAuthenticationToken(userName, rawPwd, user.getAuthorities());
        }
           throw  new BadCredentialsException("Invalid Password");

    }
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
