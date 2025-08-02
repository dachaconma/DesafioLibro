package com.aluracursos.desafio2.repository;

import com.aluracursos.desafio2.Model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :anio AND (a.fechaFallecimiento IS NULL OR a.fechaFallecimiento >= :anio)")
    List<Autor> findAutoresVivosEnAnio(Integer anio);
}