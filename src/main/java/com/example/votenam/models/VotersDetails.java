package com.example.votenam.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "voters_details")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotersDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(unique = true, nullable = false)
    private String nationalIdNumber;
    
 
    private LocalDate dateOfBirth;
    
  
    private String address;
    
    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Regions region;
    
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    
    @Column(unique = true)
    private String email;
    
    @Column(nullable = false)
    private String votersIdNumber;
    
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidates candidate;
    
    @ManyToOne
    @JoinColumn(name = "vote_category_id", nullable = false)
    private VoteCategory voteCategory;
    
    @Column(nullable = false)
    private LocalDate voteDate = LocalDate.now();
}
