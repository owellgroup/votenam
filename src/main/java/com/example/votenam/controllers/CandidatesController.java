package com.example.votenam.controllers;

import com.example.votenam.models.Candidates;
import com.example.votenam.services.CandidatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/candidates")
@CrossOrigin(origins = "*")
public class CandidatesController {
    
    @Autowired
    private CandidatesService candidatesService;
    
    // Create Candidate
    @PostMapping
    public ResponseEntity<?> createCandidate(
            @RequestParam("fullName") String fullName,
            @RequestParam("partyName") String partyName,
            @RequestParam("position") String position,
            @RequestParam("voteCategoryId") Long voteCategoryId,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "partyLogo", required = false) MultipartFile partyLogo) {
        try {
            Candidates candidate = new Candidates();
            candidate.setFullName(fullName);
            candidate.setPartyName(partyName);
            candidate.setPosition(position);
            
            com.example.votenam.models.VoteCategory voteCategory = new com.example.votenam.models.VoteCategory();
            voteCategory.setId(voteCategoryId);
            candidate.setVoteCategory(voteCategory);
            
            Candidates saved = candidatesService.createCandidate(candidate, photo, partyLogo);
            return ResponseEntity.ok(Map.of("success", true, "candidate", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get All Candidates
    @GetMapping
    public ResponseEntity<?> getAllCandidates() {
        try {
            List<Candidates> candidates = candidatesService.getAllCandidates();
            return ResponseEntity.ok(Map.of("success", true, "candidates", candidates));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get Candidate by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCandidateById(@PathVariable Long id) {
        try {
            Optional<Candidates> candidate = candidatesService.getCandidateById(id);
            if (candidate.isPresent()) {
                return ResponseEntity.ok(Map.of("success", true, "candidate", candidate.get()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Candidate not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Update Candidate
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCandidate(
            @PathVariable Long id,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "partyName", required = false) String partyName,
            @RequestParam(value = "position", required = false) String position,
            @RequestParam(value = "voteCategoryId", required = false) Long voteCategoryId,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "partyLogo", required = false) MultipartFile partyLogo) {
        try {
            Candidates candidate = new Candidates();
            if (fullName != null) candidate.setFullName(fullName);
            if (partyName != null) candidate.setPartyName(partyName);
            if (position != null) candidate.setPosition(position);
            if (voteCategoryId != null) {
                com.example.votenam.models.VoteCategory voteCategory = new com.example.votenam.models.VoteCategory();
                voteCategory.setId(voteCategoryId);
                candidate.setVoteCategory(voteCategory);
            }
            
            Candidates updated = candidatesService.updateCandidate(id, candidate, photo, partyLogo);
            return ResponseEntity.ok(Map.of("success", true, "candidate", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Delete Candidate
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCandidate(@PathVariable Long id) {
        try {
            candidatesService.deleteCandidate(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Candidate deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}

