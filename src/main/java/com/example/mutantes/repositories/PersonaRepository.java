package com.example.mutantes.repositories;

import com.example.mutantes.repositories.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

  @Query("SELECT COUNT(m) FROM Persona m WHERE m.esMutante = ?1")
  long countByEsMutante(boolean esMutante);
}
