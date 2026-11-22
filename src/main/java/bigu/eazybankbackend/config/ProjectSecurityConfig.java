package bigu.eazybankbackend.config;

import bigu.eazybankbackend.exception.CustomAccessDeniedHandler;
import bigu.eazybankbackend.filter.CsrfCookieFilter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("!prd")
@Configuration
/*
@EnableConfigurationProperties tem como função registrar a classe anotada com
 @ConfigurationProperties no contêiner Spring e torná-la um bean gerenciáve
 */
@RequiredArgsConstructor //Gera um construtor com apenas os campos final
//@AllArgsConstructor //Gera um construtor com TODOS os campos, independente de serem final, @NonNull ou não
public class ProjectSecurityConfig {

     @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
      String introspectionUri;

      @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
      String clientId;

      @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
      String clientSecret;

      /*
      KeyCloak issue:INTROSPECT_TOKEN_ERROR,

      error="invalid_token"

    reason="Client 'eazybankintrospect' is not in the token audience"

    the aud field must have the clientId as well. see the below video to know how to solve it

       https://www.youtube.com/watch?v=G2QVhUAEylc
       */

    @Bean
    public SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler(); //Expor o token durante o request, usado somente na primeira request , ou seja, quando um novo token é gerado.
        //JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        //jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleConverter());
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

        //http.oauth2ResourceServer(rsc-> rsc.jwt(jwtConfig->jwtConfig.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        http.oauth2ResourceServer(rsc->rsc.opaqueToken(
                opt-> opt.authenticationConverter(new KeycloakOpaqueRoleConverter())
                        .introspectionUri(this.introspectionUri).introspectionClientCredentials(this.clientId, this.clientSecret)
        ));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }



}
