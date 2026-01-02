package com.jumder.ulloa.repository;

import com.jumder.ulloa.entity.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CanchaRepository extends JpaRepository<Cancha, Long> {
    List<Cancha> findByPolideportivoId(Long polideportivoId);
    List<Cancha> findByPolideportivoIdAndActivoTrue(Long polideportivoId);
    List<Cancha> findByTipo(Cancha.TipoCancha tipo);
}
