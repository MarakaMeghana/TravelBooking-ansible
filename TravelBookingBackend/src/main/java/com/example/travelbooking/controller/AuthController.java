package com.example.travelbooking.controller;

import com.example.travelbooking.model.User;
import com.example.travelbooking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")  // Allow frontend requests (React)
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ✅ REGISTER user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            System.out.println("📩 Register attempt for: " + user.getEmail());
            System.out.println("➡️ Plain password before encoding: " + user.getPassword());

            // ⚠️ Do NOT encode password here — handled by UserService
            User savedUser = userService.register(user);
            System.out.println("✅ User saved successfully with encoded password!");

            // Hide password in response for security
            savedUser.setPassword(null);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (RuntimeException e) {
            System.out.println("❌ Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registration failed: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // ✅ LOGIN user
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            Optional<User> userOpt = userService.findByEmail(loginRequest.getEmail());

            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // Debug info
                System.out.println("🔹 Login attempt for email: " + loginRequest.getEmail());
                System.out.println("🔹 Entered password (plain): " + loginRequest.getPassword());
                System.out.println("🔹 Stored password hash: " + user.getPassword());

                boolean matches = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword()).isPresent();
                System.out.println("🔹 Password match result: " + matches);

                if (matches) {
                    System.out.println("✅ Password matched successfully!");
                    user.setPassword(null); // Don't expose password
                    return ResponseEntity.ok(user);
                } else {
                    System.out.println("❌ Invalid password entered");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Invalid password");
                }

            } else {
                System.out.println("❌ User not found: " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error during login: " + e.getMessage());
        }
    }

    // ✅ Optional: Fetch user by email
    @GetMapping("/user")
    public ResponseEntity<?> findUser(@RequestParam String email) {
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
