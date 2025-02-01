package com.example.aparts.repositories.autoparts;

import com.example.aparts.models.autoparts.Fasteners;
import com.example.aparts.models.autoparts.Other;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtherRepository extends JpaRepository<Other, Long> {
    Page<Other> findByNameLike(String name, Pageable pageable);
    Page<Other> findByName(String name, Pageable pageable);
}
