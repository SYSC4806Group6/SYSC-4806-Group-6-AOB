package org.example.services;

import org.example.entities.Book;
import org.example.entities.PurchaseReceipt;
import org.example.entities.PurchaseReceiptItem;
import org.example.entities.User;
import org.example.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class RecommendationServiceTest {

    private BookRepository bookRepository;
    private RecommendationService recommendationService;

    @BeforeEach
    public void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        recommendationService = new RecommendationService(bookRepository);
    }

    @Test
    public void testGetRecommendations() {
        // Setup User and Purchase History
        User user = new User();
        Book purchasedBook = new Book("1", "Dune", "SciFi Epic", Arrays.asList("SciFi", "Space"), "url",
                "Frank Herbert", "Publisher", 10.0);

        PurchaseReceipt receipt = new PurchaseReceipt(user);
        PurchaseReceiptItem item = new PurchaseReceiptItem(receipt, purchasedBook, 1, 10.0);

        // Setup Candidate Books
        Book similarBook = new Book("2", "Dune Messiah", "Sequel", Arrays.asList("SciFi", "Space"), "url",
                "Frank Herbert", "Publisher", 10.0);
        Book differentBook = new Book("3", "Pride and Prejudice", "Romance", Arrays.asList("Romance", "Classic"), "url",
                "Jane Austen", "Publisher", 10.0);
        Book somewhatSimilarBook = new Book("4", "The Martian", "Mars", Arrays.asList("SciFi", "Survival"), "url",
                "Andy Weir", "Publisher", 10.0);

        when(bookRepository.findAll())
                .thenReturn(Arrays.asList(purchasedBook, similarBook, differentBook, somewhatSimilarBook));

        // Execute
        List<Book> recommendations = recommendationService.getRecommendations(user);

        // Verify
        // similarBook should be first (Same Author + Same Tags -> High Jaccard)
        // somewhatSimilarBook should be second (One Tag match -> Lower Jaccard)
        // differentBook should not be present (0 Jaccard)

        assertEquals(2, recommendations.size());
        assertEquals(similarBook, recommendations.get(0));
        assertEquals(somewhatSimilarBook, recommendations.get(1));
    }

    @Test
    public void testGetRecommendations_NoPurchases() {
        User user = new User();
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<Book> recommendations = recommendationService.getRecommendations(user);
        assertTrue(recommendations.isEmpty());
    }
}
