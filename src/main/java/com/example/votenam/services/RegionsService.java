package com.example.votenam.services;

import com.example.votenam.models.Regions;
import com.example.votenam.repositories.RegionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegionsService {
    
    @Autowired
    private RegionsRepository regionsRepository;
    
    public Regions createRegion(Regions region) {
        return regionsRepository.save(region);
    }
    
    public List<Regions> getAllRegions() {
        return regionsRepository.findAll();
    }
    
    public Optional<Regions> getRegionById(Long id) {
        return regionsRepository.findById(id);
    }
    
    public Regions updateRegion(Long id, Regions regionDetails) {
        Regions region = regionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region not found with id: " + id));
        
        region.setRegionName(regionDetails.getRegionName());
        
        return regionsRepository.save(region);
    }
    
    public void deleteRegion(Long id) {
        if (!regionsRepository.existsById(id)) {
            throw new RuntimeException("Region not found with id: " + id);
        }
        regionsRepository.deleteById(id);
    }
}

