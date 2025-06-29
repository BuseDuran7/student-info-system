package com.ege.controller;

import com.ege.dto.AuthResponseDto;
import com.ege.dto.LoginDto;
import com.ege.entities.User;
import com.ege.repository.UserRepository;
import com.ege.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/dev/fix-passwords")
    public ResponseEntity<?> fixPlainPasswords() {
        List<User> users = userRepository.findAll();
        int updated = 0;

        for (User user : users) {
            String pw = user.getPassword();
            // Eğer şifre zaten hash’lenmişse ($2a$10$ ile başlar), geç
            if (pw != null && !pw.startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(pw));
                userRepository.save(user);
                updated++;
                System.out.println("🔐 Updated password for user: " + user.getUsername());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("updatedCount", updated);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginRequest) {
        System.out.println("Login attempt for user: " + loginRequest.getUsername());

        try {
            // Kullanıcıyı doğrula
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            System.out.println("Authentication successful for: " + loginRequest.getUsername());

            // JWT token oluştur
            String jwt = tokenProvider.generateToken(authentication);

            // Response döndür
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", jwt);
            response.put("tokenType", "Bearer");

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials for user: " + loginRequest.getUsername());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    // Current user bilgilerini getir
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());

            if (userOptional.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "User not found");
                return ResponseEntity.badRequest().body(error);
            }

            User user = userOptional.get();

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("email", user.getEmail());
            response.put("roleName", user.getUserRole().getRoleName());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    // Logout (JWT'de genelde frontend'de token silmek yeterli)
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User logged out successfully");
        return ResponseEntity.ok(response);
    }

    // Token validation endpoint
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Token is valid");
        return ResponseEntity.ok(response);
    }
}