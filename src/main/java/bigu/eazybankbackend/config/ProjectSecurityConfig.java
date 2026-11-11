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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@EnableConfigurationProperties(BcryptProperties.class)
@AllArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)//Essa anotação habilita o mecanismo de autorização nos métodos.
public class ProjectSecurityConfig {

    private final BcryptProperties bcryptProperties;

    @Bean
    public SecurityFilterChain customSecurityFilterChain(HttpSecurity http, Environment environment) {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler(); //Expor o token durante o request, usado somente na primeira request , ou seja, quando um novo token é gerado.
        //http.authorizeHttpRequests((r) -> r.anyRequest().permitAll()); //AuthorizationFilter
        //http.authorizeHttpRequests((r) -> r.anyRequest().denyAll());
        //http.sessionManagement(smc-> smc.)
        //http.sessionManagement(smc->smc.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId).invalidSessionUrl("/invalidSession").maximumSessions(1));
        //http.securityContext(contextConfig -> contextConfig.requireExplicitSave(false));//Deixar o SSecurity salvar automa. na sessão o SecurityContext no SecurityContextHolder
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));//Para forçar criar sessão (JSESSIONID) visto que agora usamos uma pág de login costumizado
        http.cors(withDefaults());
        http.csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                .ignoringRequestMatchers("/api/v1/contacts/register", "/api/v1/customers/register", "/api/v1/customers/apiLogin") //Para ignorar a proteção do CSRF
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));//Usado durante o login..
        http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class); //Depois do 'BasicAuthenticationFilter', executa o filter... Usado durente o login apenas
        http.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new AuthoritiesLoggingFilter(), BasicAuthenticationFilter.class);
        http.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new JWTTokenGeneratorFilter(environment), BasicAuthenticationFilter.class);
        http.addFilterBefore(new JWTTokenValidatorFilter(environment), BasicAuthenticationFilter.class);
        http.authorizeHttpRequests(r -> {
             /*.requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                        .requestMatchers("/myBalance").hasAnyAuthority("VIEWBALANCE", "VIEWACCOUNT")
                        .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
                        .requestMatchers("/myCards").hasAuthority("VIEWCARDS")*/
                        r.requestMatchers("/myAccount").hasRole("USER")
                    .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/myLoans").authenticated()
                    .requestMatchers("/myCards").hasRole("USER");
            r.requestMatchers( "/api/v1/customers/register").authenticated();
            r.requestMatchers("/api/v1/contacts/register", "/error", "/invalidSession", "/notices", "/api/v1/customers/apiLogin").permitAll();
            r.anyRequest().authenticated(); //é fundamental terminar as regas de auth com catch-all, caso contrário todas as outras requis serão negadas
        });
        //http.formLogin(AbstractHttpConfigurer::disable); //filtro:UsernamePasswordAuthenticationFilter...
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())); //Indicamos ao SSecurity qual entryPoint ele deve usar, apenas para httpBasic, BasicAuthenticationFilter
        //http.exceptionHandling(ehc-> ehc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));//Config global,
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        //http.csrf(AbstractHttpConfigurer::disable);

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

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        EazyBankUserPasswordAuthenticationProvider authenticationProvider =
                new EazyBankUserPasswordAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return  providerManager;
    }



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
