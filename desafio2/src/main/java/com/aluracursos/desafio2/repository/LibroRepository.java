package com.aluracursos.desafio2.repository;

import com.aluracursos.desafio2.Model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByTitulo(String titulo);
    List<Libro> findByIdiomasContaining(String idioma);
}
