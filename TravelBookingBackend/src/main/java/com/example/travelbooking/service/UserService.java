package com.example.travelbooking.service;

import com.example.travelbooking.model.User;
import com.example.travelbooking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Register a new user safely
    public User register(User user) {
        // ✅ Check if email is already registered
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already registered: " + user.getEmail());
        }

        // ✅ Encode the password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // ✅ Assign default role if none provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        return userRepository.save(user);
    }

    // ✅ Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ✅ Authenticate user (login)
    public Optional<User> authenticate(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            String storedPassword = user.getPassword();

            // ✅ Debugging logs
            System.out.println("Login attempt for email: " + email);
            System.out.println("Entered password (plain): " + rawPassword);
            System.out.println("Stored password hash: " + storedPassword);

            // ✅ Check if stored password looks hashed (starts with $2a or $2b)
            boolean isHashed = storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$");

            boolean matches = isHashed
                    ? passwordEncoder.matches(rawPassword, storedPassword)
                    : rawPassword.equals(storedPassword);

            System.out.println("Password match result: " + matches);

            if (matches) {
                return Optional.of(user);
            } else {
                System.out.println("❌ Invalid password entered");
            }
        }
        return Optional.empty();
    }
}
