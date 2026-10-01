package bigu.eazybankbackend.config;

import bigu.eazybankbackend.service.BerpUserService;
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
    private final BerpUserService berpUserService;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        var user = berpUserService.findByUserName(userName);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("Guest"));
        return new User(user.getUserName(), user.getPwd(), authorities);
    }
}
