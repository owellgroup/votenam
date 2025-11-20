package com.example.votenam.services;

import com.example.votenam.models.Candidates;
import com.example.votenam.models.VoteCategory;
import com.example.votenam.repositories.CandidatesRepository;
import com.example.votenam.repositories.VoteCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        
        return candidatesRepository.save(candidate);
    }
    
    public List<Candidates> getAllCandidates() {
        return candidatesRepository.findAll();
    }
    
    public List<Candidates> getCandidatesByVoteCategory(Long voteCategoryId) {
        return candidatesRepository.findByVoteCategoryId(voteCategoryId);
    }
    
    public Optional<Candidates> getCandidateById(Long id) {
        return candidatesRepository.findById(id);
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
        
        return candidatesRepository.save(candidate);
    }
    
    public void deleteCandidate(Long id) {
        if (!candidatesRepository.existsById(id)) {
            throw new RuntimeException("Candidate not found with id: " + id);
        }
        candidatesRepository.deleteById(id);
    }
}

