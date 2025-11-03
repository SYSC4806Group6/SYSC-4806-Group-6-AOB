package org.example.sampledata;

import org.example.entities.Book;
import org.example.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {

        bookRepository.deleteAll();

        List<Book> books = SampleBooks.getBooks();

        bookRepository.saveAll(books);

        System.out.println("Loaded " + books.size() + " sample books into the database.");
    }
}
