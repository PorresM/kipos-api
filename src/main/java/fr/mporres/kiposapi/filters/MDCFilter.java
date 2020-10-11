package fr.mporres.kiposapi.filters;

import fr.mporres.kiposapi.services.auth.AuthService;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class MDCFilter extends GenericFilterBean {
    private final AuthService authService;

    public MDCFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            MDC.put("req_id", UUID.randomUUID().toString());
            authService.getUser().ifPresent(x -> MDC.put("user_id", x.getId().toString()));
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
