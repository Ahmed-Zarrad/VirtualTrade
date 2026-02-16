package lsmsdb.unipi.it.virtualtrade.controller;

import lsmsdb.unipi.it.virtualtrade.dto.UserResponse;
import lsmsdb.unipi.it.virtualtrade.dto.UserUpdateRequest;
import lsmsdb.unipi.it.virtualtrade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String id,
            @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }
}
