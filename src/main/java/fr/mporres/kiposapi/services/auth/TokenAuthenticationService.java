package fr.mporres.kiposapi.services.auth;

import fr.mporres.kiposapi.conf.JwtProperties;
import fr.mporres.kiposapi.services.user.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

/**
 * JWT Token management service
 */
@Component
public class TokenAuthenticationService {

    private final JwtProperties jwtProperties;
    private final UserService userService;

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer";

    public TokenAuthenticationService(JwtProperties jwtProperties, UserService userService) {
        this.jwtProperties = jwtProperties;
        this.userService = userService;
    }

    /**
     * Authentication fetching service from a request
     * @param request
     * @return
     */
    public Optional<UserAuthentication> getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(HEADER_STRING);
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return Optional.empty();
        }

        String userId = Jwts.parser()
            .setSigningKey(jwtProperties.getSecret())
            .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
            .getBody()
            .getSubject();

        if (userId == null) {
            return Optional.empty();
        }

        return userService.getUserById(Long.valueOf(userId)).map(UserAuthentication::new);
    }

    /**
     * Token creation method
     * @param idUser
     * @return
     */
    String createNewToken(Long idUser) {
        return Jwts.builder()
            .setSubject(idUser.toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
            .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
            .compact();
    }
}
