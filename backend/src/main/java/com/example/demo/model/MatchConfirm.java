package com.example.demo.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "MatchConfirm")
public class MatchConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String playerName;
    @Column(nullable = false)
    private String matchId;





}