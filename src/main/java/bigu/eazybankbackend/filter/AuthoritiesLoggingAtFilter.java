package bigu.eazybankbackend.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class AuthoritiesLoggingAtFilter implements Filter { //addFilterAfter
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            log.info("Authentication Validation is in progress...");

        chain.doFilter(request, response);
    }
}
