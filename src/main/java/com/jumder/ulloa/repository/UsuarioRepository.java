package com.jumder.ulloa.repository;

import com.jumder.ulloa.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// ==================== USUARIO REPOSITORY ====================
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCedula(String cedula);
    List<Usuario> findByPolideportivoId(Long polideportivoId);
    List<Usuario> findByRol(Usuario.RolUsuario rol);
    List<Usuario> findByActivoTrue();
}