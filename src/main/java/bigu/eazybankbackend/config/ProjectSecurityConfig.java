package bigu.eazybankbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {

        http.authorizeHttpRequests(r -> {
            r.requestMatchers("/secure").authenticated();
            r.anyRequest().permitAll();
        });
        http.formLogin(withDefaults()); //filtro:UsernamePasswordAuthenticationFilter...
        http.httpBasic(withDefaults()); //Filtro:BasicAuthenticationFilter...
        /*
        oauth2Login() é o ponto de entrada no Spring
        Security para transformar sua aplicação em um cliente que
         delega a autenticação de usuários a provedores externos (Google, Facebook..), cuidando de
        todo o fluxo de redirecionamento, troca de tokens e criação da sessão do usuário.
         */
        http.oauth2Login(withDefaults()); //O método configura a aplicação para iniciar o fluxo de concessão de código de autorização.

        return http.build();
    }


    //Armazena apenas as informações (clientId e clientSecret) de configuração dos
    // provedores (clientes OAuth 2.0), Google, Facebook...
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
                googleClientRegistration(),
                githubClientRegistration()
        );
    }


    private ClientRegistration googleClientRegistration() {
        return CommonOAuth2Provider.GOOGLE
                .getBuilder("google")
                .clientId("SEU_GOOGLE_CLIENT_ID")
                .clientSecret("SEU_GOOGLE_CLIENT_ID")
                .build();
    }

    /*
     private ClientRegistration facebookClientRegistration() {
        return CommonOAuth2Provider.FACEBOOK
                .getBuilder("facebook")
                .clientId("SEU_FACEBOOK_CLIENT_ID")
                .clientSecret("SEU_FACEBOOK_CLIENT_SECRET")
                .build();
    }
     */


    @Bean
    public ClientRegistration githubClientRegistration() {
        return CommonOAuth2Provider.GITHUB
                .getBuilder("github")          // registrationId em minúsculo, consistente com o padrão
                .clientId("SEU_GITHUB_CLIENT_ID")
                .clientSecret("SEU_GITHUB_CLIENT_SECRET")
                .clientName("GitHub")          // opcional, nome de exibição
                .build();
    }
}
