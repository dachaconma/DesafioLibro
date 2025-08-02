package service;

import com.aluracursos.desafio2.Model.Autor;
import com.aluracursos.desafio2.repository.AutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {
    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public Autor guardarAutor(Autor autor) {
        return autorRepository.save(autor);
    }

    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresVivosEnAnio(Integer anio) {
        return autorRepository.findAutoresVivosEnAnio(anio);
    }
}
