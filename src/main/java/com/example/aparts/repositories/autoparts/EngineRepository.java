package com.example.aparts.repositories.autoparts;

import com.example.aparts.models.autoparts.Chassis;
import com.example.aparts.models.autoparts.Engine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EngineRepository extends JpaRepository<Engine, Long> {
    Page<Engine> findByNameLike(String name, Pageable pageable);
    Page<Engine> findByName(String name, Pageable pageable);
}
