package bigu.eazybankbackend.repository;

import bigu.eazybankbackend.model.BerpUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BerpUserRepository extends CrudRepository<BerpUser, String> {

    Optional<BerpUser> findByUserName(String userName);
}
