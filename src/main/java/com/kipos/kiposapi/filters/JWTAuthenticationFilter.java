package com.kipos.kiposapi.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kipos.kiposapi.exception.ErrorResponse;
import com.kipos.kiposapi.services.auth.TokenAuthenticationService;
import com.kipos.kiposapi.services.auth.UserAuthentication;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Optional;

/**
 * Check Authentication for filtered request
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

    private final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final TokenAuthenticationService tokenAuthenticationService;
    private final ObjectMapper objectMapper;

    public JWTAuthenticationFilter(TokenAuthenticationService tokenAuthenticationService, ObjectMapper objectMapper) {
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            Optional<UserAuthentication> authentication = tokenAuthenticationService.getAuthentication((HttpServletRequest) request);
            authentication.ifPresent(userAuthentication -> SecurityContextHolder.getContext().setAuthentication(userAuthentication));
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | NumberFormatException e) {
            LOGGER.info("Authentication Unauthorized: {}", e.getMessage());

            HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
            ErrorResponse errorResponse = ErrorResponse.builder()
                .path(((HttpServletRequest) request).getRequestURI())
                .message(e.getMessage())
                .status(httpStatus.value())
                .timestamp(new Date())
                .error(httpStatus.getReasonPhrase())
                .build();

            HttpServletResponse httpResp = (HttpServletResponse) response;
            httpResp.setStatus(httpStatus.value());
            httpResp.setContentType("application/json; charset=UTF-8");
            PrintWriter writer = httpResp.getWriter();
            writer.write(objectMapper.writeValueAsString(errorResponse));
            writer.flush();
        }

    }
}
