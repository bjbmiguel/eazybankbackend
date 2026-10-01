package bigu.eazybankbackend.service;

import bigu.eazybankbackend.model.Customer;
import bigu.eazybankbackend.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@AllArgsConstructor
public class CustomerService {
    /*
    Importante: final torna a referência imutável, mas não o objeto em si.
    Os métodos do objeto ainda podem ser chamados normalmente.
    this.customerRepository = outroRepository; // ❌ Erro de compilação
     */
    private final CustomerRepository customerRepository;

    @Transactional
    public Customer register(Customer customer){
        Assert.notNull(customer, "Customer cannot be bull");
        return customerRepository.save(customer);
    }

    public Customer findByEmail(String email){
        Assert.notNull(email, "Email cannot be bull");
        return customerRepository.findByEmail(email).
                orElseThrow(()->new UsernameNotFoundException("User details not found for the user: " + email));

    }
}
