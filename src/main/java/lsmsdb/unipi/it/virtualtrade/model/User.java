package lsmsdb.unipi.it.virtualtrade.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

import java.time.Instant;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;
    @NotNull
    @NotBlank(message = "First name must not be empty!")
    private String firstName;

    @NotNull
    @NotBlank(message = "Last name must not be empty!")
    private String lastName;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    @NotNull
    @NotBlank(message = "The email must not be empty!")
    @Email(message = "Wrong email format.")
    private String email;

    @NotNull
    @NotEmpty(message = "The password must not be empty!")
    private String passwordHash;

    private Instant createdAt = Instant.now();

    private boolean emailVerified = false;

}