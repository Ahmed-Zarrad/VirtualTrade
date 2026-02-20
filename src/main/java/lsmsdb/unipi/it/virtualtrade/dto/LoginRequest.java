package lsmsdb.unipi.it.virtualtrade.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username or Email is required")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    private String password;
}
