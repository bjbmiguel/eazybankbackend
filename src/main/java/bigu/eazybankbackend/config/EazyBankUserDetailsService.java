package bigu.eazybankbackend.config;

import bigu.eazybankbackend.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EazyBankUserDetailsService implements UserDetailsService {

    private final CustomerService customerService;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        var user = customerService.findByEmail(userName);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
        return new User(user.getEmail(), user.getPwd(), authorities);
    }
}
