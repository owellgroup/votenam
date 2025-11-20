package com.example.votenam.repositories;

import com.example.votenam.models.Candidates;
import com.example.votenam.models.VoteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatesRepository extends JpaRepository<Candidates, Long> {
    List<Candidates> findByVoteCategory(VoteCategory voteCategory);
    List<Candidates> findByVoteCategoryId(Long voteCategoryId);
}

