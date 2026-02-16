package lsmsdb.unipi.it.virtualtrade.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String role;
    private Instant createdAt;
    private boolean emailVerified;
}