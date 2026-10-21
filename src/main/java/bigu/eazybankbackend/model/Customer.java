package bigu.eazybankbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Integer id;

    @Column(nullable = false, length = 100)
    @ToString.Include
    private String name;

    @Column(nullable = false, length = 100)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "mobile_number", nullable = false, length = 20)
    private String mobileNumber;

    @Column(nullable = false, length = 500)
    //@JsonIgnore //Excluir completamente uma propriedade
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Não inclui a propriedade durante serialização (Object->Json) mas permite receber um valor durante a deserealização (Json ->object)
    private String pwd;

    @Column(nullable = false, length = 100)
    private String role;

    @Column(name = "create_dt")
    private LocalDate createDt;
}