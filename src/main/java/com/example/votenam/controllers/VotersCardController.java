package com.example.votenam.controllers;

import com.example.votenam.models.VotersCard;
import com.example.votenam.services.VotersCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/voters-cards")
@CrossOrigin(origins = "*")
public class VotersCardController {
    
    @Autowired
    private VotersCardService votersCardService;
    
    // Create Voter Card
    @PostMapping
    public ResponseEntity<?> createVotersCard(@RequestBody VotersCard votersCard) {
        try {
            VotersCard saved = votersCardService.createVotersCard(votersCard);
            return ResponseEntity.ok(Map.of("success", true, "votersCard", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get All Voter Cards
    @GetMapping
    public ResponseEntity<?> getAllVotersCards() {
        try {
            List<VotersCard> cards = votersCardService.getAllVotersCards();
            return ResponseEntity.ok(Map.of("success", true, "votersCards", cards));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get Voter Card by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getVotersCardById(@PathVariable Long id) {
        try {
            Optional<VotersCard> card = votersCardService.getVotersCardById(id);
            if (card.isPresent()) {
                return ResponseEntity.ok(Map.of("success", true, "votersCard", card.get()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Voter Card not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Update Voter Card
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVotersCard(@PathVariable Long id, @RequestBody VotersCard votersCard) {
        try {
            VotersCard updated = votersCardService.updateVotersCard(id, votersCard);
            return ResponseEntity.ok(Map.of("success", true, "votersCard", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Delete Voter Card
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVotersCard(@PathVariable Long id) {
        try {
            votersCardService.deleteVotersCard(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Voter Card deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}

