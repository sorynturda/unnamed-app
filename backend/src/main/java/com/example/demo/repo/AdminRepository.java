package com.example.demo.repo;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<User, String> {
}
