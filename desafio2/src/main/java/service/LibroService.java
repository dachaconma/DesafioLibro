package service;

import com.aluracursos.desafio2.Model.Libro;
import com.aluracursos.desafio2.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {
    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public Libro guardarLibro(Libro libro) {
        if (libroRepository.existsByTitulo(libro.getTitulo())) {
            throw new RuntimeException("El libro ya existe en la base de datos");
        }
        return libroRepository.save(libro);
    }

    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    public List<Libro> listarLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdiomasContaining(idioma);
    }
}
