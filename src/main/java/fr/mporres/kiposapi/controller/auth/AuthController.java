package fr.mporres.kiposapi.controller.auth;

import fr.mporres.kiposapi.controller.auth.request.LoginRequest;
import fr.mporres.kiposapi.controller.auth.response.LoginResponse;
import fr.mporres.kiposapi.controller.user.response.UserResponse;
import fr.mporres.kiposapi.exception.ApiException;
import fr.mporres.kiposapi.persistence.entity.User;
import fr.mporres.kiposapi.services.auth.AuthService;
import fr.mporres.kiposapi.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * User authentication WS
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * login WS
     */
    @PostMapping(value = "/login")
    public @ResponseBody
    LoginResponse login(@RequestBody LoginRequest requestUser) {
        User dbUser;
        if (requestUser.getEmail() == null || requestUser.getPassword() == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials");
        }

        dbUser = userService.getUserByEmail(requestUser.getEmail()).orElseThrow(
            () -> new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials")
        );

        return authService.getToken(dbUser, requestUser.getPassword())
            .map(
                token -> LoginResponse.builder()
                .token(token)
                .user(
                    UserResponse.builder()
                        .email(dbUser.getEmail())
                        .role(dbUser.getRole().name())
                        .build()
                ).build()
            ).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials"));
    }
}
