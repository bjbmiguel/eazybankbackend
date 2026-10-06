package bigu.eazybankbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor //Gera um construtor apenas para os campos obrigatórios, ou seja:campos final e anotados como @NonNull
//@AllArgsConstructor //Gera um construtor com todos os campos da classe, independentemente de serem final ou não
public class EazyBankUserPasswordAuthenticationProvider {
    private final UserDetailsService userDetailsService;

}
