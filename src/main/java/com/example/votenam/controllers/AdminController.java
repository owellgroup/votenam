package com.example.votenam.controllers;

import com.example.votenam.models.Users;
import com.example.votenam.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private UsersService usersService;
    
    // Admin Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            
            if (email == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email and password are required"));
            }
            
            Optional<Users> user = usersService.login(email, password);
            if (user.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", user.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid email or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Create User
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody Users user) {
        try {
            Users savedUser = usersService.createUser(user);
            return ResponseEntity.ok(Map.of("success", true, "user", savedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get All Users
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<Users> users = usersService.getAllUsers();
            return ResponseEntity.ok(Map.of("success", true, "users", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get User by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<Users> user = usersService.getUserById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(Map.of("success", true, "user", user.get()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Update User
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Users user) {
        try {
            Users updatedUser = usersService.updateUser(id, user);
            return ResponseEntity.ok(Map.of("success", true, "user", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Delete User
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            usersService.deleteUser(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}

