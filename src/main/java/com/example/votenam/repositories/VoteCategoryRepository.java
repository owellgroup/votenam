package com.example.votenam.repositories;

import com.example.votenam.models.VoteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteCategoryRepository extends JpaRepository<VoteCategory, Long> {
}

