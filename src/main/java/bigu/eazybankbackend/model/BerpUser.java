package bigu.eazybankbackend.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class BerpUser {

    @Id
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "username")
    private String userName;

    @Column(name = "password", length = 200)
    private String pwd;

    private boolean enabled;
}
