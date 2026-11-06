package bigu.eazybankbackend.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class RequestValidationBeforeFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        String header = httpRequest.getHeader(
                HttpHeaders.AUTHORIZATION
        );

        if (StringUtils.hasText(header)
                && StringUtils.startsWithIgnoreCase(header, "Basic ")) {

            try {
                String credentials = header.substring(6).trim();

                byte[] decoded = Base64.getDecoder()
                        .decode(credentials);

                String token = new String(
                        decoded, StandardCharsets.UTF_8
                );

                int delim = token.indexOf(":");

                if (delim == -1) {
                    throw new BadCredentialsException(
                            "Invalid basic authentication token"
                    );
                }

                String email = token.substring(0, delim);

                if (email.toLowerCase().contains("test")) {
                    httpResponse.sendError(
                            HttpServletResponse.SC_BAD_REQUEST
                    );
                    return;
                }

            } catch (IllegalArgumentException e) {
                throw new BadCredentialsException(
                        "Failed to decode Basic authentication token", e
                );
            }
        }

        // Continue to the next filter
        chain.doFilter(request, response);
    }
}

