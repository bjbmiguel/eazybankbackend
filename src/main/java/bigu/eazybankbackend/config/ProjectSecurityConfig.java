package bigu.eazybankbackend.config;

import com.password4j.Argon2Function;
import com.password4j.types.Argon2;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password4j.Argon2Password4jPasswordEncoder;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import javax.sql.DataSource;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
/*
@EnableConfigurationProperties tem como função registrar a classe anotada com
 @ConfigurationProperties no contêiner Spring e torná-la um bean gerenciáve
 */
@EnableConfigurationProperties(BcryptProperties.class)
@AllArgsConstructor
public class ProjectSecurityConfig {

    private final BcryptProperties bcryptProperties;

    @Bean
    public SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {

        //http.authorizeHttpRequests((r) -> r.anyRequest().permitAll()); //AuthorizationFilter
        //http.authorizeHttpRequests((r) -> r.anyRequest().denyAll());
        http.authorizeHttpRequests(r -> {
            r.requestMatchers("/api/v1/welcome", "/api/v1/loans").authenticated();
            r.requestMatchers("/api/v1/contacts", "/error", "/users/register").permitAll();
            r.anyRequest().authenticated(); //é fundamental terminar as regas de auth com catch-all, caso contrário todas as outras requis serão negadas
        });
        //http.formLogin(AbstractHttpConfigurer::disable); //filtro:UsernamePasswordAuthenticationFilter...
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults()); //Filtro:BasicAuthenticationFilter...
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /*
                                            java.lang.IllegalArgumentException: Given that there is
                                            no default password encoder configured, each password must have a password encoding prefix. Please either prefix this password with '{noop}'
                                     or set a default password encoder in `DelegatingPasswordEncoder`.
                                 */

    //criamos dois users em memória (Map)


    /*
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
     */




    /*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B, 12, new SecureRandom());
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();//Gera uma instância default (bcrypt) de PasswordEncoder
    }
*/

    /*

    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "argon2";//Novas passwords serão sempre geradas usando Argon2.
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put(idForEncode, new Argon2Password4jPasswordEncoder(Argon2Function.getInstance(16_384, 2,1, 32, Argon2.ID, 19)));//Dev
        //encoders.put(idForEncode, new Argon2Password4jPasswordEncoder(Argon2Function.getInstance(65_536, 3,2, 32, Argon2.ID, 19))); //PRD
       return  new DelegatingPasswordEncoder(idForEncode, encoders);
    }
     */


     @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

 /*
           Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put(
                "bcrypt",
                new BCryptPasswordEncoder(version, bcryptProperties.getStrength(), new SecureRandom())
        );

        return new DelegatingPasswordEncoder("bcrypt", encoders);

         */
}
