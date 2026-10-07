package bigu.eazybankbackend.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("prd")
@Configuration
/*
@EnableConfigurationProperties tem como função registrar a classe anotada com
 @ConfigurationProperties no contêiner Spring e torná-la um bean gerenciável
 */
@EnableConfigurationProperties(BcryptProperties.class)
@AllArgsConstructor
public class ProjectPrdSecurityConfig {

    private final BcryptProperties bcryptProperties;

    @Bean
    public SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {

        //http.authorizeHttpRequests((r) -> r.anyRequest().permitAll()); //AuthorizationFilter
        //http.authorizeHttpRequests((r) -> r.anyRequest().denyAll());
        http.redirectToHttps(withDefaults());//Configura um redirecionamento para Https
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
