package com.main.aqaradmin.repository;

import com.main.aqaradmin.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByEmail(String email);
    boolean existsByEmail(String email);
}
