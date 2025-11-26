package org.example.services;

import org.example.entities.Book;
import org.example.entities.PurchaseReceipt;
import org.example.entities.PurchaseReceiptItem;
import org.example.entities.User;
import org.example.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final BookRepository bookRepository;

    public RecommendationService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getRecommendations(User user) {
        if (user == null) {
            return Collections.emptyList();
        }

        // Gets all books purchased by the user
        Set<Book> purchasedBooks = user.getPurchaseReceipts().stream()
                .flatMap(receipt -> receipt.getItems().stream())
                .map(PurchaseReceiptItem::getBook)
                .collect(Collectors.toSet());

        if (purchasedBooks.isEmpty()) {
            return Collections.emptyList();
        }

        // Gets all available books
        List<Book> allBooks = bookRepository.findAll();

        // Filters out books the user has already bought
        List<Book> candidateBooks = allBooks.stream()
                .filter(book -> !purchasedBooks.contains(book))
                .collect(Collectors.toList());

        // Calculates similarity scores
        Map<Book, Double> scores = new HashMap<>();
        for (Book candidate : candidateBooks) {
            double totalScore = 0.0;
            for (Book purchased : purchasedBooks) {
                totalScore += calculateJaccardSimilarity(candidate, purchased);
            }
            if (totalScore > 0) {
                scores.put(candidate, totalScore);
            }
        }

        // Sorts candidates by score (descending) and returns top N
        return scores.entrySet().stream()
                .sorted(Map.Entry.<Book, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double calculateJaccardSimilarity(Book b1, Book b2) {
        Set<String> set1 = new HashSet<>();
        set1.add(b1.getAuthor());
        set1.addAll(b1.getTags());

        Set<String> set2 = new HashSet<>();
        set2.add(b2.getAuthor());
        set2.addAll(b2.getTags());

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) intersection.size() / union.size();
    }
}
