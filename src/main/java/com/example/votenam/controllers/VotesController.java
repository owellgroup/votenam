package com.example.votenam.controllers;

import com.example.votenam.models.VotersDetails;
import com.example.votenam.services.VotersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/votes")
@CrossOrigin(origins = "*")
public class VotesController {
    
    @Autowired
    private VotersDetailsService votersDetailsService;
    
    // Get All Votes
    @GetMapping
    public ResponseEntity<?> getAllVotes() {
        try {
            List<VotersDetails> votes = votersDetailsService.getAllVotes();
            return ResponseEntity.ok(Map.of("success", true, "votes", votes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get Vote by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getVoteById(@PathVariable Long id) {
        try {
            return votersDetailsService.getVoteById(id)
                    .map(vote -> ResponseEntity.ok(Map.of("success", true, "vote", vote)))
                    .orElse(ResponseEntity.badRequest().body(Map.of("success", false, "message", "Vote not found")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get Votes by Candidate
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<?> getVotesByCandidate(@PathVariable Long candidateId) {
        try {
            List<VotersDetails> votes = votersDetailsService.getVotesByCandidate(candidateId);
            return ResponseEntity.ok(Map.of("success", true, "votes", votes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get Votes by Vote Category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getVotesByCategory(@PathVariable Long categoryId) {
        try {
            List<VotersDetails> votes = votersDetailsService.getVotesByVoteCategory(categoryId);
            return ResponseEntity.ok(Map.of("success", true, "votes", votes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}

