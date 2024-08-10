package com.example.demo.repo;

import com.example.demo.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, String> {

}
