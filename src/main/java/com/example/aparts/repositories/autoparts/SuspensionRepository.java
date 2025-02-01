package com.example.aparts.repositories.autoparts;

import com.example.aparts.models.autoparts.Other;
import com.example.aparts.models.autoparts.Suspension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuspensionRepository extends JpaRepository<Suspension, Long> {
    Page<Suspension> findByNameLike(String name, Pageable pageable);
    Page<Suspension> findByName(String name, Pageable pageable);
}
