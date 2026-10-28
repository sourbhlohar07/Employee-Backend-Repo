package com.example.Aivra_Backend.repositry;

import com.example.Aivra_Backend.entity.AdminDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminDetail, Long> {
    Optional<AdminDetail> findByEmailIgnoreCase(String email);
}
