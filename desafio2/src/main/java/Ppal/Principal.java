package Ppal;

import com.aluracursos.desafio2.Model.Datos;
import com.aluracursos.desafio2.Model.DatosLibros;
import com.aluracursos.desafio2.Model.Libro;
import org.springframework.stereotype.Component;
import service.AutorService;
import service.LibroService;
import service.consumoAPI;
import service.convertDats;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private consumoAPI consumAPI = new consumoAPI();
    private convertDats conversor = new convertDats();
    private Scanner teclado = new Scanner(System.in);
    private LibroService libroService;
    private AutorService autorService;

    public Principal(LibroService libroService, AutorService autorService) {
        this.libroService = libroService;
        this.autorService = autorService;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            System.out.println("""
                    \n
                    Elija la opción a través de su número:
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """);
            try {
                opcion = Integer.parseInt(teclado.nextLine());
                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEnAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema... by Diego Chacon");
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido");
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el nombre del libro a buscar:");
        var tituloLibro = teclado.nextLine();
        var json = consumAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        if (datosBusqueda.resultados().isEmpty()) {
            System.out.println("No se encontraron libros con ese título");
            return;
        }

        DatosLibros libroEncontrado = datosBusqueda.resultados().get(0);
        System.out.println("\nLibro encontrado:");
        System.out.println("Título: " + libroEncontrado.titulo());
        System.out.println("Autor(es): " + libroEncontrado.autor().stream()
                .map(a -> a.nombre())
                .collect(Collectors.joining(", ")));
        System.out.println("Idioma(s): " + String.join(", ", libroEncontrado.idiomas()));
        System.out.println("Número de descargas: " + libroEncontrado.numDescargas());

        // Guardar en BD
        Libro libro = new Libro();
        libro.setTitulo(libroEncontrado.titulo());
        libro.setIdiomas(libroEncontrado.idiomas());
        libro.setNumDescargas(libroEncontrado.numDescargas());

        try {
            libroService.guardarLibro(libro);
            System.out.println("Libro guardado en la base de datos");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listarLibrosRegistrados() {
        var libros = libroService.listarLibros();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos");
            return;
        }

        System.out.println("\nLibros registrados:");
        libros.forEach(libro -> {
            System.out.println("\nTítulo: " + libro.getTitulo());
            System.out.println("Idioma(s): " + String.join(", ", libro.getIdiomas()));
            System.out.println("Número de descargas: " + libro.getNumDescargas());
        });
    }

    private void listarAutoresRegistrados() {
        var autores = autorService.listarAutores();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos");
            return;
        }

        System.out.println("\nAutores registrados:");
        autores.forEach(autor -> {
            System.out.println("\nNombre: " + autor.getNombre());
            System.out.println("Fecha de nacimiento: " + autor.getFechaNacimiento());
            System.out.println("Fecha de fallecimiento: " + autor.getFechaFallecimiento());
        });
    }

    private void listarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año para buscar autores vivos:");
        try {
            int anio = Integer.parseInt(teclado.nextLine());
            var autores = autorService.listarAutoresVivosEnAnio(anio);

            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores vivos en ese año");
                return;
            }

            System.out.println("\nAutores vivos en el año " + anio + ":");
            autores.forEach(autor -> {
                System.out.println("\nNombre: " + autor.getNombre());
                System.out.println("Fecha de nacimiento: " + autor.getFechaNacimiento());
                System.out.println("Fecha de fallecimiento: " + autor.getFechaFallecimiento());
            });
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un año válido");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                \nSeleccione el idioma:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);
        var idioma = teclado.nextLine().toLowerCase();

        if (!List.of("es", "en", "fr", "pt").contains(idioma)) {
            System.out.println("Idioma no válido");
            return;
        }

        var libros = libroService.listarLibrosPorIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No hay libros en " + obtenerNombreIdioma(idioma));
            return;
        }

        System.out.println("\nLibros en " + obtenerNombreIdioma(idioma) + ":");
        libros.forEach(libro -> {
            System.out.println("\nTítulo: " + libro.getTitulo());
            System.out.println("Número de descargas: " + libro.getNumDescargas());
        });
    }

    private String obtenerNombreIdioma(String codigo) {
        return switch (codigo) {
            case "es" -> "Español";
            case "en" -> "Inglés";
            case "fr" -> "Francés";
            case "pt" -> "Portugués";
            default -> "Desconocido";
        };
    }
}