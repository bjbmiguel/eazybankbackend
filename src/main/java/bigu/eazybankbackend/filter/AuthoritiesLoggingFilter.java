package bigu.eazybankbackend.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class AuthoritiesLoggingFilter implements Filter { //addFilterAfter
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication)) {
            log.info("User " + authentication.getName() + " is successfully authenticated and has the authorities" + authentication.getAuthorities());
        }
        chain.doFilter(request, response);
    }
}
