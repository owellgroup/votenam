package com.example.votenam.repositories;

import com.example.votenam.models.VotersCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotersCardRepository extends JpaRepository<VotersCard, Long> {
    Optional<VotersCard> findByCardNumber(String cardNumber);
    boolean existsByCardNumber(String cardNumber);
}

