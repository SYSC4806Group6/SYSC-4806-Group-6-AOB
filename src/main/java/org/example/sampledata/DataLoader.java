package org.example.sampledata;

import org.example.entities.Book;
import org.example.entities.User;
import org.example.repositories.BookRepository;
import org.example.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SampleUsers sampleUsers;

    @Override
    public void run(String... args) throws Exception {

        bookRepository.deleteAll();
        userRepository.deleteAll();

        List<Book> books = SampleBooks.getBooks();
        List<User> users = sampleUsers.getUsers();

        bookRepository.saveAll(books);
        userRepository.saveAll(users);

        System.out.println("Loaded " + books.size() + " sample books into the database.");
    }
}
