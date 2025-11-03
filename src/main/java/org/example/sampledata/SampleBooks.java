package org.example.sampledata;

import org.example.entities.Book;

import java.util.ArrayList;
import java.util.List;

public class SampleBooks {

    public static List<Book> getBooks() {
        return List.of(
                new Book(
                        "978-0743273565",
                        "The Great Gatsby",
                        "A novel about the Jazz Age.",
                        new ArrayList<>(List.of("Fiction", "Classic")),
                        "url2.jpg",
                        "F. Scott Fitzgerald",
                        "Scribner",
                        14.99
                ),
                new Book(
                        "978-0439023528",
                        "The Hunger Games",
                        "A dystopian novel set in Panem.",
                        new ArrayList<>(List.of("Fiction", "Dystopian")),
                        "url3.jpg",
                        "Suzanne Collins",
                        "Scholastic Press",
                        12.99
                ),
                new Book(
                        "978-0316769488",
                        "The Catcher in the Rye",
                        "A story about teenage angst and alienation.",
                        new ArrayList<>(List.of("Fiction", "Classic")),
                        "url4.jpg",
                        "J.D. Salinger",
                        "Little, Brown and Company",
                        10.99
                ),
                new Book(
                        "978-0141439600",
                        "Pride and Prejudice",
                        "A classic novel about love and society.",
                        new ArrayList<>(List.of("Fiction", "Romance", "Classic")),
                        "url5.jpg",
                        "Jane Austen",
                        "Penguin Classics",
                        9.99
                ),
                new Book(
                        "978-0553293357",
                        "Dune",
                        "A science fiction epic about politics and power.",
                        new ArrayList<>(List.of("Fiction", "Sci-Fi")),
                        "url6.jpg",
                        "Frank Herbert",
                        "Ace",
                        15.99
                ),
                new Book(
                        "978-0061120084",
                        "To Kill a Mockingbird",
                        "A novel about racial injustice in the Deep South.",
                        new ArrayList<>(List.of("Fiction", "Classic")),
                        "url7.jpg",
                        "Harper Lee",
                        "Harper Perennial",
                        13.99
                ),
                new Book(
                        "978-0385490818",
                        "The Alchemist",
                        "A journey of self-discovery and following your dreams.",
                        new ArrayList<>(List.of("Fiction", "Adventure", "Philosophy")),
                        "url9.jpg",
                        "Paulo Coelho",
                        "HarperOne",
                        11.99
                ),
                new Book(
                        "978-0307346605",
                        "The Road",
                        "A post-apocalyptic tale of survival and hope.",
                        new ArrayList<>(List.of("Fiction", "Dystopian")),
                        "url10.jpg",
                        "Cormac McCarthy",
                        "Vintage",
                        14.49
                ),
                new Book(
                        "978-0679783275",
                        "1984",
                        "A dystopian novel about totalitarianism and surveillance.",
                        new ArrayList<>(List.of("Fiction", "Dystopian", "Classic")),
                        "url11.jpg",
                        "George Orwell",
                        "Plume",
                        12.49
                )
        );
    }
}
