package com.example.demo.repo;

import com.example.demo.model.MatchConfirm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmRepo extends JpaRepository<MatchConfirm,String> {
}
