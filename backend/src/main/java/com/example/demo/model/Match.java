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
@Table(name = "matches")
public class Match {

    @Id
    @Column(nullable = false)
    private String name;

    private Date date;

    private String referee;

    private String player1;

    private String player2;

    private String score;


}