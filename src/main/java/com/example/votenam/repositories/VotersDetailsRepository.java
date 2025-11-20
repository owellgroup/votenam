package com.example.votenam.repositories;

import com.example.votenam.models.VotersDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotersDetailsRepository extends JpaRepository<VotersDetails, Long> {
    boolean existsByNationalIdNumber(String nationalIdNumber);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByVotersIdNumber(String votersIdNumber);
    Optional<VotersDetails> findByNationalIdNumber(String nationalIdNumber);
}

