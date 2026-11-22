package bigu.eazybankbackend.config;

import bigu.eazybankbackend.exception.CustomAccessDeniedHandler;
import bigu.eazybankbackend.exception.CustomBasicAuthenticationEntryPoint;
import bigu.eazybankbackend.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("!prd")
@Configuration
/*
@EnableConfigurationProperties tem como função registrar a classe anotada com
 @ConfigurationProperties no contêiner Spring e torná-la um bean gerenciáve
 */
@AllArgsConstructor
public class ProjectSecurityConfig {


    @Bean
    public SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler(); //Expor o token durante o request, usado somente na primeira request , ou seja, quando um novo token é gerado.
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleConverter());
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));//Para forçar criar sessão (JSESSIONID) visto que agora usamos uma pág de login costumizado
        http.cors(withDefaults());
        http.csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));//Usado durante o login..
        http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class); //Depois do 'BasicAuthenticationFilter', executa o filter... Usado durente o login apenas
        http.authorizeHttpRequests(r -> {
                        r.requestMatchers("/myAccount").hasRole("USER")
                    .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/myLoans").hasRole("USER")
                    .requestMatchers("/myCards").hasRole("USER");
            r.requestMatchers( "/api/v1/customers/register").authenticated();
            r.requestMatchers("/api/v1/contacts/register", "/error", "/invalidSession", "/notices").permitAll();
            r.anyRequest().authenticated(); //é fundamental terminar as regas de auth com catch-all, caso contrário todas as outras requis serão negadas
        });

        http.oauth2ResourceServer(rsc-> rsc.jwt(jwtConfig->jwtConfig.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }






}
