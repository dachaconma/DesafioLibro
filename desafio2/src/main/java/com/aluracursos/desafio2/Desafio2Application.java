package com.aluracursos.desafio2;

import Ppal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.aluracursos.desafio2", "Ppal", "service"})
public class Desafio2Application implements CommandLineRunner {

	private final Principal principal;

	public Desafio2Application(Principal principal) {
		this.principal = principal;
	}

	public static void main(String[] args) {
		SpringApplication.run(Desafio2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.muestraElMenu();
	}
}