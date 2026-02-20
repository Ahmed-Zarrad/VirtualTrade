package lsmsdb.unipi.it.virtualtrade.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "First name must not be empty!")
    private String firstName;

    @NotBlank(message = "Last name must not be empty!")
    private String lastName;

    @NotBlank(message = "Username must not be empty!")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "The email must not be empty!")
    @Email(message = "Wrong email format.")
    private String email;

    @NotBlank(message = "The password must not be empty!")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}