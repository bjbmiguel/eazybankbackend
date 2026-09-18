package bigu.eazybankbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {

        //http.authorizeHttpRequests((r) -> r.anyRequest().permitAll()); //AuthorizationFilter
        //http.authorizeHttpRequests((r) -> r.anyRequest().denyAll());
        http.authorizeHttpRequests(r -> {
            r.requestMatchers("/api/v1/welcome", "/api/v1/loans").authenticated();
            r.requestMatchers("/api/v1/contacts", "/error").permitAll();
        });
        http.formLogin(AbstractHttpConfigurer::disable); //filtro:UsernamePasswordAuthenticationFilter...
        http.httpBasic(withDefaults()); //Filtro:BasicAuthenticationFilter...

        return http.build();
    }
}
