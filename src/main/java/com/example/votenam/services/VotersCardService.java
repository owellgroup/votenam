package com.example.votenam.services;

import com.example.votenam.models.VotersCard;
import com.example.votenam.repositories.VotersCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VotersCardService {
    
    @Autowired
    private VotersCardRepository votersCardRepository;
    
    public VotersCard createVotersCard(VotersCard votersCard) {
        if (votersCardRepository.existsByCardNumber(votersCard.getCardNumber())) {
            throw new RuntimeException("Voter Card with number " + votersCard.getCardNumber() + " already exists");
        }
        return votersCardRepository.save(votersCard);
    }
    
    public List<VotersCard> getAllVotersCards() {
        return votersCardRepository.findAll();
    }
    
    public Optional<VotersCard> getVotersCardById(Long id) {
        return votersCardRepository.findById(id);
    }
    
    public VotersCard updateVotersCard(Long id, VotersCard votersCardDetails) {
        VotersCard votersCard = votersCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voter Card not found with id: " + id));
        
        // Check if card number is being changed and if new card number already exists
        if (!votersCard.getCardNumber().equals(votersCardDetails.getCardNumber()) && 
            votersCardRepository.existsByCardNumber(votersCardDetails.getCardNumber())) {
            throw new RuntimeException("Voter Card with number " + votersCardDetails.getCardNumber() + " already exists");
        }
        
        votersCard.setCardNumber(votersCardDetails.getCardNumber());
        votersCard.setCardName(votersCardDetails.getCardName());
        
        return votersCardRepository.save(votersCard);
    }
    
    public void deleteVotersCard(Long id) {
        if (!votersCardRepository.existsById(id)) {
            throw new RuntimeException("Voter Card not found with id: " + id);
        }
        votersCardRepository.deleteById(id);
    }
    
    public boolean validateCardNumber(String cardNumber) {
        return votersCardRepository.existsByCardNumber(cardNumber);
    }
    
    public Optional<VotersCard> getVotersCardByCardNumber(String cardNumber) {
        return votersCardRepository.findByCardNumber(cardNumber);
    }
}

