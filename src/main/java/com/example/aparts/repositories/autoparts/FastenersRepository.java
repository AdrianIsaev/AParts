package com.example.aparts.repositories.autoparts;

import com.example.aparts.models.autoparts.Engine;
import com.example.aparts.models.autoparts.Fasteners;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FastenersRepository extends JpaRepository<Fasteners, Long> {
    Page<Fasteners> findByNameLike(String name, Pageable pageable);
    Page<Fasteners> findByName(String name, Pageable pageable);
}
