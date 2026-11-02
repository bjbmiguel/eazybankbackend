package bigu.eazybankbackend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfCookieFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrfToken =
                (CsrfToken) request.getAttribute(
                        CsrfToken.class.getName()
                );

        System.out.println("csrfToken = " + csrfToken);

        if (csrfToken != null) {
            String tk = csrfToken.getToken();
            System.out.println("Token = " + tk);
        }
        filterChain.doFilter(request, response);

    }
}
