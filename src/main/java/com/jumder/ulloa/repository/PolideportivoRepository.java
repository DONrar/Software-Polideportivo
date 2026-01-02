package com.jumder.ulloa.repository;

import com.jumder.ulloa.Polideportivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PolideportivoRepository extends JpaRepository<Polideportivo, Long> {
    List<Polideportivo> findByActivoTrue();
    List<Polideportivo> findByCiudad(String ciudad);
    List<Polideportivo> findByDepartamento(String departamento);
}
