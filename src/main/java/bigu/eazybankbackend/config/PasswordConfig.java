package bigu.eazybankbackend.config;

import com.password4j.Argon2Function;
import com.password4j.types.Argon2;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password4j.Argon2Password4jPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(Argon2Properties.class)
@AllArgsConstructor
public class PasswordConfig {
    public static final int ARGON2_VERSION = 19;
    private final Argon2Properties properties;

    @Bean
    public PasswordEncoder passwordEncoder(){

        String idForEncode = "argon2";//Novas passwords serão sempre geradas usando Argon2.

        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put("bcrypt", new BCryptPasswordEncoder()); //permite que o sistema continue validando passwords antigas armazenadas como: {bcrypt}

        Argon2Function argon2Function = Argon2Function.getInstance(
                properties.memory(),
                properties.iterations(),
                properties.parallelism(),
                properties.hashLength(),
                Argon2.ID,
                ARGON2_VERSION
        );

        encoders.put(
                idForEncode,
                new Argon2Password4jPasswordEncoder(argon2Function)
        );

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }
}
