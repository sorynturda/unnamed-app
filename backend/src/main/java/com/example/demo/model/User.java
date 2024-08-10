package com.example.demo.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    private String name;
    @Getter
    @Column(nullable = false)
    private String password;
    @Column
    private Role role;
    @Column
    private String email;



}