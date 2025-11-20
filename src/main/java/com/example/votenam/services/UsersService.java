package com.example.votenam.services;

import com.example.votenam.models.Users;
import com.example.votenam.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    
    @Autowired
    private UsersRepository usersRepository;
    
    public Users createUser(Users user) {
        if (usersRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        return usersRepository.save(user);
    }
    
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }
    
    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }
    
    public Users updateUser(Long id, Users userDetails) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Check if email is being changed and if new email already exists
        if (!user.getEmail().equals(userDetails.getEmail()) && 
            usersRepository.existsByEmail(userDetails.getEmail())) {
            throw new RuntimeException("User with email " + userDetails.getEmail() + " already exists");
        }
        
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setUsername(userDetails.getUsername());
        user.setRole(userDetails.getRole());
        
        return usersRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        usersRepository.deleteById(id);
    }
    
    public Optional<Users> login(String email, String password) {
        return usersRepository.findByEmailAndPassword(email, password);
    }
    
    public boolean validateLogin(String email, String password) {
        return usersRepository.findByEmailAndPassword(email, password).isPresent();
    }
}

