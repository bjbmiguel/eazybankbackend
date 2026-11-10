package bigu.eazybankbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {//Bean com políticas CORS.
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));//permite o JS do browser enviar o JWT e outros headers no request
        config.setExposedHeaders(List.of("Authorization"));//permite o JS browser ler o JWT no response.


        // Ativar apenas se a aplicação precisar de credenciais CORS.
        config.setAllowCredentials(true);//em requisições cors, podemos enviar cookies/Id de sessão/CSRF tokens

        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
