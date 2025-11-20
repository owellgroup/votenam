package com.example.votenam.services;

import com.example.votenam.models.*;
import com.example.votenam.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VotersDetailsService {
    
    @Autowired
    private VotersDetailsRepository votersDetailsRepository;
    
    @Autowired
    private VotersCardRepository votersCardRepository;
    
    @Autowired
    private CandidatesRepository candidatesRepository;
    
    @Autowired
    private VoteCategoryRepository voteCategoryRepository;
    
    @Autowired
    private RegionsRepository regionsRepository;
    
    @Autowired
    private EmailService emailService;
    
    public VotersDetails submitVote(VotersDetails votersDetails) {
        // Validate Voters ID Number exists in VotersCard FIRST (before other validations)
        if (!votersCardRepository.existsByCardNumber(votersDetails.getVotersIdNumber())) {
            throw new RuntimeException("Card number does not exist");
        }
        
        // Validate National ID is unique
        if (votersDetailsRepository.existsByNationalIdNumber(votersDetails.getNationalIdNumber())) {
            throw new RuntimeException("Already exist");
        }
        
        // Validate Email is unique
        if (votersDetailsRepository.existsByEmail(votersDetails.getEmail())) {
            throw new RuntimeException("Already exist");
        }
        
        // Validate Phone Number is unique
        if (votersDetailsRepository.existsByPhoneNumber(votersDetails.getPhoneNumber())) {
            throw new RuntimeException("Already exist");
        }
        
        // Validate Voters ID Number is unique (one vote per card)
        if (votersDetailsRepository.existsByVotersIdNumber(votersDetails.getVotersIdNumber())) {
            throw new RuntimeException("Already exist");
        }
        
        // Validate and set related entities
        Candidates candidate = candidatesRepository.findById(votersDetails.getCandidate().getId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        
        VoteCategory voteCategory = voteCategoryRepository.findById(votersDetails.getVoteCategory().getId())
                .orElseThrow(() -> new RuntimeException("Vote Category not found"));
        
        Regions region = regionsRepository.findById(votersDetails.getRegion().getId())
                .orElseThrow(() -> new RuntimeException("Region not found"));
        
        votersDetails.setCandidate(candidate);
        votersDetails.setVoteCategory(voteCategory);
        votersDetails.setRegion(region);
        
        // Save the vote
        VotersDetails savedVote = votersDetailsRepository.save(votersDetails);
        
        // Send confirmation email
        try {
            emailService.sendVoteConfirmationEmail(
                savedVote.getFullName(),
                savedVote.getEmail(),
                candidate.getFullName(),
                voteCategory.getCategoryName()
            );
        } catch (Exception e) {
            // Log error but don't fail the vote submission
            System.err.println("Failed to send confirmation email: " + e.getMessage());
        }
        
        return savedVote;
    }
    
    public List<VotersDetails> getAllVotes() {
        return votersDetailsRepository.findAll();
    }
    
    public Optional<VotersDetails> getVoteById(Long id) {
        return votersDetailsRepository.findById(id);
    }
    
    public List<VotersDetails> getVotesByCandidate(Long candidateId) {
        Candidates candidate = candidatesRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        return votersDetailsRepository.findAll().stream()
                .filter(vote -> vote.getCandidate().getId().equals(candidateId))
                .toList();
    }
    
    public List<VotersDetails> getVotesByVoteCategory(Long voteCategoryId) {
        return votersDetailsRepository.findAll().stream()
                .filter(vote -> vote.getVoteCategory().getId().equals(voteCategoryId))
                .toList();
    }
}

