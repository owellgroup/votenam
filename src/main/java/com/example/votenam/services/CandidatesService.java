package com.example.votenam.services;

import com.example.votenam.models.Candidates;
import com.example.votenam.models.VoteCategory;
import com.example.votenam.repositories.CandidatesRepository;
import com.example.votenam.repositories.VoteCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CandidatesService {
    
    @Autowired
    private CandidatesRepository candidatesRepository;
    
    @Autowired
    private VoteCategoryRepository voteCategoryRepository;
    
    @Autowired
    private FileUploadService fileUploadService;
    
    @Value("${app.base.url:https://vote.owellgraphics.com}")
    private String baseUrl;
    
    private void transformUrls(Candidates candidate) {
        if (candidate.getPhotoUrl() != null && candidate.getPhotoUrl().contains("localhost")) {
            String photoUrl = candidate.getPhotoUrl();
            // Extract filename from URL
            String fileName = photoUrl.substring(photoUrl.lastIndexOf("/") + 1);
            candidate.setPhotoUrl(baseUrl + "/api/photos/view/" + fileName);
        }
        if (candidate.getPartyLogoUrl() != null && candidate.getPartyLogoUrl().contains("localhost")) {
            String logoUrl = candidate.getPartyLogoUrl();
            // Extract filename from URL
            String fileName = logoUrl.substring(logoUrl.lastIndexOf("/") + 1);
            candidate.setPartyLogoUrl(baseUrl + "/api/logos/view/" + fileName);
        }
    }
    
    public Candidates createCandidate(Candidates candidate, MultipartFile photo, MultipartFile partyLogo) throws IOException {
        VoteCategory voteCategory = voteCategoryRepository.findById(candidate.getVoteCategory().getId())
                .orElseThrow(() -> new RuntimeException("Vote Category not found"));
        
        candidate.setVoteCategory(voteCategory);
        
        if (photo != null && !photo.isEmpty()) {
            String photoUrl = fileUploadService.uploadPhoto(photo);
            candidate.setPhotoUrl(photoUrl);
        }
        
        if (partyLogo != null && !partyLogo.isEmpty()) {
            String logoUrl = fileUploadService.uploadLogo(partyLogo);
            candidate.setPartyLogoUrl(logoUrl);
        }
        
        Candidates saved = candidatesRepository.save(candidate);
        transformUrls(saved);
        return saved;
    }
    
    public List<Candidates> getAllCandidates() {
        List<Candidates> candidates = candidatesRepository.findAll();
        candidates.forEach(this::transformUrls);
        return candidates;
    }
    
    public List<Candidates> getCandidatesByVoteCategory(Long voteCategoryId) {
        List<Candidates> candidates = candidatesRepository.findByVoteCategoryId(voteCategoryId);
        candidates.forEach(this::transformUrls);
        return candidates;
    }
    
    public Optional<Candidates> getCandidateById(Long id) {
        Optional<Candidates> candidate = candidatesRepository.findById(id);
        candidate.ifPresent(this::transformUrls);
        return candidate;
    }
    
    public Candidates updateCandidate(Long id, Candidates candidateDetails, MultipartFile photo, MultipartFile partyLogo) throws IOException {
        Candidates candidate = candidatesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));
        
        candidate.setFullName(candidateDetails.getFullName());
        candidate.setPartyName(candidateDetails.getPartyName());
        candidate.setPosition(candidateDetails.getPosition());
        
        if (candidateDetails.getVoteCategory() != null && candidateDetails.getVoteCategory().getId() != null) {
            VoteCategory voteCategory = voteCategoryRepository.findById(candidateDetails.getVoteCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Vote Category not found"));
            candidate.setVoteCategory(voteCategory);
        }
        
        if (photo != null && !photo.isEmpty()) {
            String photoUrl = fileUploadService.uploadPhoto(photo);
            candidate.setPhotoUrl(photoUrl);
        }
        
        if (partyLogo != null && !partyLogo.isEmpty()) {
            String logoUrl = fileUploadService.uploadLogo(partyLogo);
            candidate.setPartyLogoUrl(logoUrl);
        }
        
        Candidates saved = candidatesRepository.save(candidate);
        transformUrls(saved);
        return saved;
    }
    
    public void deleteCandidate(Long id) {
        if (!candidatesRepository.existsById(id)) {
            throw new RuntimeException("Candidate not found with id: " + id);
        }
        candidatesRepository.deleteById(id);
    }
}

