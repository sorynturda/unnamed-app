package com.example.demo.repo;

import com.example.demo.model.Match;
import com.example.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefereeRepository extends JpaRepository<User, String> {

}
