package com.ProyectoGPS.Backend.repository;

import com.ProyectoGPS.Backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByUsername(String username);
    
    Optional<Usuario> findByEmail(String email);
    
    List<Usuario> findByActivo(Boolean activo);
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    long countActiveUsers();
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.ultimoAcceso >= :fechaInicio")
    long countUsersWithRecentAccess(@Param("fechaInicio") LocalDateTime fechaInicio);
    
    List<Usuario> findByRol(Usuario.Rol rol);
}
