package bigu.eazybankbackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.bcrypt")
@Getter
@Setter
public class BcryptProperties {
    private String version = "2B";
    private int strength = 12;
}
