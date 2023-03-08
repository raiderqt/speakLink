package com.example.LearningDevProject1.repository;

import com.example.LearningDevProject1.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
