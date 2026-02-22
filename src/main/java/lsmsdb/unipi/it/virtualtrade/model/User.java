package lsmsdb.unipi.it.virtualtrade.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

import java.time.Instant;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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
    @NotNull
    @NotBlank(message = "Username must not be empty!")
    private String username;
    @NotNull(message = "Role cannot be null")
    private Role role;
    @Indexed(unique = true)
    @NotNull
    @NotBlank(message = "The email must not be empty!")
    @Email(message = "Wrong email format.")
    private String email;

    @NotNull
    @NotEmpty(message = "The password must not be empty!")
    private String passwordHash;
    @CreatedDate
    private Instant createdAt = Instant.now();

    private boolean emailVerified = false;
}