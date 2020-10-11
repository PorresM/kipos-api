package fr.mporres.kiposapi.controller.user.request;

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
@JsonDeserialize(builder = CreateUserRequest.CreateUserRequestBuilder.class)
public class CreateUserRequest {
    @NotNull(message = "Email mandatory")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", message = "Invalid email")
    @Size(min = 5, max = 255, message = "Invalid email")
    private final String email;

    @NotNull(message = "Password mandatory")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9]).*", message = "Invalid password")
    @Size(min = 8, max = 256, message = "Invalid password")
    private final String password;

    @NotNull(message = "Role mandatory")
    private final String role;

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    static class CreateUserRequestBuilder {}
}
