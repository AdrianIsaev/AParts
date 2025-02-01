package com.example.aparts.repositories.autoparts;

import com.example.aparts.models.autoparts.Chassis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChassisRepository extends JpaRepository<Chassis, Long> {
    Page<Chassis> findByNameLike(String name, Pageable pageable);
    Page<Chassis> findByName(String name, Pageable pageable);
}
