package com.example.votenam.services;

import com.example.votenam.models.VoteCategory;
import com.example.votenam.repositories.VoteCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteCategoryService {
    
    @Autowired
    private VoteCategoryRepository voteCategoryRepository;
    
    public VoteCategory createVoteCategory(VoteCategory voteCategory) {
        return voteCategoryRepository.save(voteCategory);
    }
    
    public List<VoteCategory> getAllVoteCategories() {
        return voteCategoryRepository.findAll();
    }
    
    public Optional<VoteCategory> getVoteCategoryById(Long id) {
        return voteCategoryRepository.findById(id);
    }
    
    public VoteCategory updateVoteCategory(Long id, VoteCategory voteCategoryDetails) {
        VoteCategory voteCategory = voteCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vote Category not found with id: " + id));
        
        voteCategory.setCategoryName(voteCategoryDetails.getCategoryName());
        
        return voteCategoryRepository.save(voteCategory);
    }
    
    public void deleteVoteCategory(Long id) {
        if (!voteCategoryRepository.existsById(id)) {
            throw new RuntimeException("Vote Category not found with id: " + id);
        }
        voteCategoryRepository.deleteById(id);
    }
}

