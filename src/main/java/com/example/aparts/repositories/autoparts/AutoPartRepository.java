package com.example.aparts.repositories.autoparts;

import com.example.aparts.models.autoparts.AutoPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoPartRepository extends JpaRepository<AutoPart, Long> {
}
