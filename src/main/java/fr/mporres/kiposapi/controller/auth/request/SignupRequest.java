package fr.mporres.kiposapi.controller.auth.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@JsonDeserialize(builder = SignupRequest.SignupRequestBuilder.class)
public class SignupRequest {
    @NotNull(message = "Email mandatory")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", message = "Invalid email")
    @Size(min = 5, max = 256, message = "Invalid email")
    private final String email;

    @NotNull(message = "Password mandatory")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9]).*", message = "Invalid password")
    @Size(min = 8, max = 256, message = "Invalid password")
    private final String password;

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    static class SignupRequestBuilder {}
}
