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

    public ShoppingCart getOrCreateCart(User user) {
        return cartRepo.findByUser(user).orElseGet(() -> {
            ShoppingCart newCart = new ShoppingCart(user);
            return cartRepo.save(newCart);
        });
    }

    public void addBookToCart(User user, String isbn) {
        ShoppingCart cart = getOrCreateCart(user);
        Book book = bookRepo.findByIsbn(isbn);
        if (book == null) return;

        ShoppingCartItem newItem = new ShoppingCartItem(cart, book);

        if (!cart.addShoppingCartItem(newItem)) {
            // Book already existed, quantity incremented inside addShoppingCartItem
        } else {
            newItem.setShoppingCart(cart);
            itemRepo.save(newItem);
        }
        cartRepo.save(cart);
    }

    public void updateQuantity(User user, String isbn, int quantity) {
        ShoppingCart cart = getOrCreateCart(user);
        cart.getItems().stream()
                .filter(i -> i.getBook().getIsbn().equals(isbn))
                .findFirst()
                .ifPresent(i -> {
                    i.setQuantity(quantity);
                    itemRepo.save(i);
                });
    }

    public void removeBook(User user, String isbn) {
        ShoppingCart cart = getOrCreateCart(user);
        cart.getItems().removeIf(i -> i.getBook().getIsbn().equals(isbn));
        cartRepo.save(cart);
    }

    public double getTotal(User user) {
        ShoppingCart cart = getOrCreateCart(user);
        return cart.getAndCalculateTotalCartPrice();
    }
}
