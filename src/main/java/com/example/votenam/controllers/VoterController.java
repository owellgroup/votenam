package com.example.votenam.controllers;

import com.example.votenam.models.VoteCategory;
import com.example.votenam.models.Candidates;
import com.example.votenam.models.VotersDetails;
import com.example.votenam.services.VoteCategoryService;
import com.example.votenam.services.CandidatesService;
import com.example.votenam.services.VotersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/voter")
@CrossOrigin(origins = "*")
public class VoterController {
    
    @Autowired
    private VoteCategoryService voteCategoryService;
    
    @Autowired
    private CandidatesService candidatesService;
    
    @Autowired
    private VotersDetailsService votersDetailsService;
    
    @Autowired
    private com.example.votenam.services.RegionsService regionsService;
    
    // Root endpoint
    @GetMapping
    public ResponseEntity<?> getRoot() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Namibia Voting System - Voter API");
        response.put("endpoints", Map.of(
            "dashboard", "/api/voter/dashboard",
            "regions", "/api/voter/regions",
            "candidates", "/api/voter/category/{categoryId}/candidates",
            "submitVote", "POST /api/voter/submit-vote"
        ));
        return ResponseEntity.ok(response);
    }
    
    // Get dashboard - all available voting categories
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        try {
            List<VoteCategory> categories = voteCategoryService.getAllVoteCategories();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("categories", categories);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get candidates by vote category
    @GetMapping("/category/{categoryId}/candidates")
    public ResponseEntity<?> getCandidatesByCategory(@PathVariable Long categoryId) {
        try {
            List<Candidates> candidates = candidatesService.getCandidatesByVoteCategory(categoryId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("candidates", candidates);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get all regions for dropdown
    @GetMapping("/regions")
    public ResponseEntity<?> getAllRegions() {
        try {
            List<com.example.votenam.models.Regions> regions = regionsService.getAllRegions();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("regions", regions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Submit vote
    @PostMapping("/submit-vote")
    public ResponseEntity<?> submitVote(@RequestBody VotersDetails votersDetails) {
        try {
            VotersDetails savedVote = votersDetailsService.submitVote(votersDetails);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Your vote has been successfully submitted. A confirmation email has been sent to your email address.");
            response.put("vote", savedVote);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}

