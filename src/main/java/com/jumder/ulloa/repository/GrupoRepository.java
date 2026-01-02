package com.jumder.ulloa.repository;

import com.jumder.ulloa.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// ==================== GRUPO REPOSITORY ====================
@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByTorneoId(Long torneoId);
}