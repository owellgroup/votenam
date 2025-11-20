package com.example.votenam.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "candidates")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Candidates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String partyName;
    
    @Column(nullable = false)
    private String position;
    
    private String photoUrl;
    
    private String partyLogoUrl;
    
    @ManyToOne
    @JoinColumn(name = "vote_category_id", nullable = false)
    private VoteCategory voteCategory;
}
