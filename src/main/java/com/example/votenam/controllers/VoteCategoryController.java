package com.example.votenam.controllers;

import com.example.votenam.models.VoteCategory;
import com.example.votenam.services.VoteCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/vote-categories")
@CrossOrigin(origins = "*")
public class VoteCategoryController {
    
    @Autowired
    private VoteCategoryService voteCategoryService;
    
    // Create Vote Category
    @PostMapping
    public ResponseEntity<?> createVoteCategory(@RequestBody VoteCategory voteCategory) {
        try {
            VoteCategory saved = voteCategoryService.createVoteCategory(voteCategory);
            return ResponseEntity.ok(Map.of("success", true, "voteCategory", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get All Vote Categories
    @GetMapping
    public ResponseEntity<?> getAllVoteCategories() {
        try {
            List<VoteCategory> categories = voteCategoryService.getAllVoteCategories();
            return ResponseEntity.ok(Map.of("success", true, "voteCategories", categories));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get Vote Category by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getVoteCategoryById(@PathVariable Long id) {
        try {
            Optional<VoteCategory> category = voteCategoryService.getVoteCategoryById(id);
            if (category.isPresent()) {
                return ResponseEntity.ok(Map.of("success", true, "voteCategory", category.get()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Vote Category not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Update Vote Category
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVoteCategory(@PathVariable Long id, @RequestBody VoteCategory voteCategory) {
        try {
            VoteCategory updated = voteCategoryService.updateVoteCategory(id, voteCategory);
            return ResponseEntity.ok(Map.of("success", true, "voteCategory", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Delete Vote Category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoteCategory(@PathVariable Long id) {
        try {
            voteCategoryService.deleteVoteCategory(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Vote Category deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}

