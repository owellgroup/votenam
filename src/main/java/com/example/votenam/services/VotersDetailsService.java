package com.example.votenam.services;

import com.example.votenam.models.*;
import com.example.votenam.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${app.base.url:https://vote.owellgraphics.com}")
    private String baseUrl;
    
    private void transformCandidateUrls(VotersDetails vote) {
        if (vote.getCandidate() != null) {
            Candidates candidate = vote.getCandidate();
            if (candidate.getPhotoUrl() != null && candidate.getPhotoUrl().contains("localhost")) {
                String photoUrl = candidate.getPhotoUrl();
                String fileName = photoUrl.substring(photoUrl.lastIndexOf("/") + 1);
                candidate.setPhotoUrl(baseUrl + "/api/photos/view/" + fileName);
            }
            if (candidate.getPartyLogoUrl() != null && candidate.getPartyLogoUrl().contains("localhost")) {
                String logoUrl = candidate.getPartyLogoUrl();
                String fileName = logoUrl.substring(logoUrl.lastIndexOf("/") + 1);
                candidate.setPartyLogoUrl(baseUrl + "/api/logos/view/" + fileName);
            }
        }
    }
    
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
        
        // Transform candidate URLs
        transformCandidateUrls(savedVote);
        
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
        List<VotersDetails> votes = votersDetailsRepository.findAll();
        votes.forEach(this::transformCandidateUrls);
        return votes;
    }
    
    public Optional<VotersDetails> getVoteById(Long id) {
        Optional<VotersDetails> vote = votersDetailsRepository.findById(id);
        vote.ifPresent(this::transformCandidateUrls);
        return vote;
    }
    
    public List<VotersDetails> getVotesByCandidate(Long candidateId) {
        Candidates candidate = candidatesRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        List<VotersDetails> votes = votersDetailsRepository.findAll().stream()
                .filter(vote -> vote.getCandidate().getId().equals(candidateId))
                .toList();
        votes.forEach(this::transformCandidateUrls);
        return votes;
    }
    
    public List<VotersDetails> getVotesByVoteCategory(Long voteCategoryId) {
        List<VotersDetails> votes = votersDetailsRepository.findAll().stream()
                .filter(vote -> vote.getVoteCategory().getId().equals(voteCategoryId))
                .toList();
        votes.forEach(this::transformCandidateUrls);
        return votes;
    }
}

