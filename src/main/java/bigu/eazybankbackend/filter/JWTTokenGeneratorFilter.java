package bigu.eazybankbackend.filter;

import bigu.eazybankbackend.constants.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;
    public  JWTTokenGeneratorFilter(Environment environment){
        String secret = environment.getRequiredProperty(
                ApplicationConstants.JWT_SECRET
        );

        byte[] keyBytes = Decoders.BASE64.decode(secret); //Descodificamos a key, ou seja, para recuperaramos os bytes originais
        //String original = new String(keyBytes, StandardCharsets.UTF_8);
        //System.out.println("Original Key "+ original);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);//Validamos a key e criamos uma inst de SK com o Algoritimo a ser usado

    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(Objects.nonNull(authentication) && authentication.isAuthenticated()){


                Instant now = Instant.now();
                List<String> lsAuth = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();
            System.out.println(lsAuth);
                String jwt = Jwts.builder()
                        .issuer("https://auth.eazybank.com")
                        .subject(authentication.getName())
                        .claim("authorities",
                                lsAuth)
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(now.plus(5, ChronoUnit.MINUTES)))
                        .signWith(this.secretKey)
                        .compact();
                response.setHeader(
                        ApplicationConstants.JWT_HEADER,jwt); //"Bearer "

            }

        filterChain.doFilter(request, response);//Executa o próx filtro.
    }

    /*
     @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !("POST".equalsIgnoreCase(request.getMethod())
                && "/api/v1/customers/user".equals(request.getServletPath()));
    }
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !("/api/v1/customers/user".equals(request.getServletPath()));
    }
}
