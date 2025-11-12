package org.example.services;

import org.example.entities.*;
import org.example.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShoppingCartService {

    private final ShoppingCartRepository cartRepo;
    private final ShoppingCartItemRepository itemRepo;
    private final BookRepository bookRepo;

    public ShoppingCartService(ShoppingCartRepository cartRepo,
                               ShoppingCartItemRepository itemRepo,
                               BookRepository bookRepo) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.bookRepo = bookRepo;
    }

    public void addBookToCart(ShoppingCart cart, Book book) {
        ShoppingCartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getBook().getIsbn().equals(book.getIsbn()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            cart.addShoppingCartItem(new ShoppingCartItem(cart, book));
        }
    }

    public int getTotalItemCount(ShoppingCart cart) {
        return cart.getItems().stream()
                .mapToInt(ShoppingCartItem::getQuantity)
                .sum();
    }
}
