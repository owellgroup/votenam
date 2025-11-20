package com.example.votenam.controllers;

import com.example.votenam.models.Regions;
import com.example.votenam.services.RegionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/regions")
@CrossOrigin(origins = "*")
public class RegionsController {
    
    @Autowired
    private RegionsService regionsService;
    
    // Create Region
    @PostMapping
    public ResponseEntity<?> createRegion(@RequestBody Regions region) {
        try {
            Regions saved = regionsService.createRegion(region);
            return ResponseEntity.ok(Map.of("success", true, "region", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get All Regions
    @GetMapping
    public ResponseEntity<?> getAllRegions() {
        try {
            List<Regions> regions = regionsService.getAllRegions();
            return ResponseEntity.ok(Map.of("success", true, "regions", regions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Get Region by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRegionById(@PathVariable Long id) {
        try {
            Optional<Regions> region = regionsService.getRegionById(id);
            if (region.isPresent()) {
                return ResponseEntity.ok(Map.of("success", true, "region", region.get()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Region not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Update Region
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRegion(@PathVariable Long id, @RequestBody Regions region) {
        try {
            Regions updated = regionsService.updateRegion(id, region);
            return ResponseEntity.ok(Map.of("success", true, "region", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    // Delete Region
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegion(@PathVariable Long id) {
        try {
            regionsService.deleteRegion(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Region deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}

