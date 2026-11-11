package bigu.eazybankbackend.filter;

import bigu.eazybankbackend.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    private final RequestMatcher excludedEndpoints =
            new OrRequestMatcher(
                    PathPatternRequestMatcher.withDefaults().matcher("/api/v1/contacts/register"),
                    PathPatternRequestMatcher.withDefaults()
                            .matcher("/api/v1/customers/user"),

                    PathPatternRequestMatcher.withDefaults()
                            .matcher("/api/v1/customers/apiLogin")
            );
    private final SecretKey secretKey;
    public  JWTTokenValidatorFilter(Environment environment){
        String secret = environment.getRequiredProperty(
                ApplicationConstants.JWT_SECRET
        );

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(ApplicationConstants.JWT_HEADER);

        try {

            if(jwt == null || !jwt.startsWith("Bearer ")) {
                throw new BadCredentialsException("Invalid token");
            }

            jwt = jwt.substring(7).trim();

            Claims claims = Jwts.parser().verifyWith(this.secretKey)
                    .build().parseSignedClaims(jwt).getPayload(); // validação da assin e dataEx acontece aqui parseSignedClaims

            if(!"https://auth.eazybank.com".equalsIgnoreCase(claims.getIssuer())){
                throw new BadCredentialsException("Invalid token, the token wasn't issued by eazybank...");
            }

            String userName = claims.getSubject();
            List<String> authorities = claims.get("authorities", List.class);

            List<GrantedAuthority> grantedAuthorities =
                    authorities.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(userName, null,
                    grantedAuthorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }catch (Exception e){
         throw  new BadCredentialsException("Cannot validate token received..."+ e.getMessage());
        }

        filterChain.doFilter(request, response);//Executa o próx filtro.

    }
/*
Uma última distinção importante: shouldNotFilter() apenas determina se o
 teu filtro personalizado será executado.
 Não concede acesso público ao endpoint.
 A autorização continua a ser controlada pela configuração
 authorizeHttpRequests() do Spring Security.
 */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
       // if(List.of("/api/v1/customers/user").contains(request.getServletPath()))
        //return ("/api/v1/customers/user".equals(request.getServletPath()));
         //return  List.of("/api/v1/customers/user", "/api/v1/customers/apiLogin").contains(request.getServletPath());

         return excludedEndpoints.matches(request);
    }
}
