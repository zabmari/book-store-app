package com.mate;

import com.mate.model.Book;
import com.mate.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class BookStoreAppApplication {

	@Autowired
	BookService bookService;

	public static void main(String[] args) {
		SpringApplication.run(BookStoreAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(){
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				Book testBook = new Book();
				testBook.setAuthor("Katarzyna Mrok");
				testBook.setTitle("Szkarłatny Szept");
				testBook.setIsbn("978-83-66-66666-6");
				testBook.setDescription("Przerażająca historia o opuszczonym domu, w którym każdej nocy słychać szepty z zaświatów. Tylko jedno jest pewne — ten, kto je usłyszy, nigdy już nie zaśnie spokojnie.");
				testBook.setPrice(BigDecimal.valueOf(66.60));
				testBook.setCoverImage("https://example.com/images/szkarlatny_szept.jpg");
				bookService.save(testBook);
				System.out.println(testBook);
			}
		};
	}

}