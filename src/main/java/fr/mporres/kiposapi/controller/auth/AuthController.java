package fr.mporres.kiposapi.controller.auth;

import fr.mporres.kiposapi.controller.auth.request.LoginRequest;
import fr.mporres.kiposapi.controller.auth.response.LoginResponse;
import fr.mporres.kiposapi.exception.ApiException;
import fr.mporres.kiposapi.services.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * User authentication WS
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * login WS
     */
    @PostMapping(value = "/login")
    public @ResponseBody
    LoginResponse login(@RequestBody LoginRequest user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials");
        }

        return authService.getToken(user.getEmail(), user.getPassword())
            .map(token -> LoginResponse.builder().token(token).build())
            .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials"));
    }
}
