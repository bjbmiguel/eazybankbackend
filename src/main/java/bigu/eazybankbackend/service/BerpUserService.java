package bigu.eazybankbackend.service;

import bigu.eazybankbackend.model.BerpUser;
import bigu.eazybankbackend.model.Customer;
import bigu.eazybankbackend.repository.BerpUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@AllArgsConstructor
public class BerpUserService {
    private final BerpUserRepository berpUserRepository;

    public BerpUser findByUserName(String userName){
        Assert.notNull(userName, "UserName cannot be bull");
        return berpUserRepository.findByUserName(userName).
                orElseThrow(()->new UsernameNotFoundException("User details not found for the user: " + userName));

    }
}
