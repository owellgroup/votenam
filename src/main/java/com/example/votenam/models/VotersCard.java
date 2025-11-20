package com.example.votenam.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "voters_card")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotersCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String cardNumber;
    
    @Column(nullable = false)
    private String cardName;
}
