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
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQGp_IGUCIXlzDZORCQ6s1J5ORF7wnHCzrHnUa7Idsg7wIj1UBN",
                        "F. Scott Fitzgerald",
                        "Scribner",
                        14.99
                ),
                new Book(
                        "978-0439023528",
                        "The Hunger Games",
                        "A dystopian novel set in Panem.",
                        new ArrayList<>(List.of("Fiction", "Dystopian")),
                        "https://upload.wikimedia.org/wikipedia/en/4/42/HungerGamesPoster.jpg",
                        "Suzanne Collins",
                        "Scholastic Press",
                        12.99
                ),
                new Book(
                        "978-0316769488",
                        "The Catcher in the Rye",
                        "A story about teenage angst and alienation.",
                        new ArrayList<>(List.of("Fiction", "Classic")),
                        "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQI4jvPBdtJkRum2UDTnbH1FNwwtmnxZu78jewYfRjFysLkB6iu",
                        "J.D. Salinger",
                        "Little, Brown and Company",
                        10.99
                ),
                new Book(
                        "978-0141439600",
                        "Pride and Prejudice",
                        "A classic novel about love and society.",
                        new ArrayList<>(List.of("Fiction", "Romance", "Classic")),
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTTNKjQgL_4DgfB9k7eLS4QuLtaqr9r7dEm50JnYPc1ono4vT5W5lQxsZiwLDMqxR6phNLcuA&s=10",
                        "Jane Austen",
                        "Penguin Classics",
                        9.99
                ),
                new Book(
                        "978-0553293357",
                        "Dune",
                        "A science fiction epic about politics and power.",
                        new ArrayList<>(List.of("Fiction", "Sci-Fi")),
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSorOTkCB7ct_eBsUiytUEILmgPT6Jo50QuBON3RQfqFHYIvf1fSqG1Q7ifYLSGzGG4P0KoHQ&s=10",
                        "Frank Herbert",
                        "Ace",
                        15.99
                ),
                new Book(
                        "978-0061120084",
                        "To Kill a Mockingbird",
                        "A novel about racial injustice in the Deep South.",
                        new ArrayList<>(List.of("Fiction", "Classic")),
                        "https://cdn.britannica.com/21/182021-050-666DB6B1/book-cover-To-Kill-a-Mockingbird-many-1961.jpg",
                        "Harper Lee",
                        "Harper Perennial",
                        13.99
                ),
                new Book(
                        "978-0385490818",
                        "The Alchemist",
                        "A journey of self-discovery and following your dreams.",
                        new ArrayList<>(List.of("Fiction", "Adventure", "Philosophy")),
                        "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcT0YogR5wxYf9funbMinznhx0V85_i28lPYmekn5RAvi-67Ns5W",
                        "Paulo Coelho",
                        "HarperOne",
                        11.99
                ),
                new Book(
                        "978-0307346605",
                        "The Road",
                        "A post-apocalyptic tale of survival and hope.",
                        new ArrayList<>(List.of("Fiction", "Dystopian")),
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-zTnlrO-iG8jUZiN3MJCqbRrASqD1D5iAtMezm7PPR44go0KB",
                        "Cormac McCarthy",
                        "Vintage",
                        14.49
                ),
                new Book(
                        "978-0679783275",
                        "1984",
                        "A dystopian novel about totalitarianism and surveillance.",
                        new ArrayList<>(List.of("Fiction", "Dystopian", "Classic")),
                        "https://m.media-amazon.com/images/I/715WdnBHqYL._UF1000,1000_QL80_.jpg",
                        "George Orwell",
                        "Plume",
                        12.49
                )
        );
    }
}
