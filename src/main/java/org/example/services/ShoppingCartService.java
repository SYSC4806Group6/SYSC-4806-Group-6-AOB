package org.example.services;

import org.example.entities.*;
import org.example.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShoppingCartService {

    private final BookService bookService;

    public ShoppingCartService(BookService bookService) {
        this.bookService = bookService;
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

    public void removeBookFromCart(ShoppingCart cart, String isbn) {
        ShoppingCartItem itemToRemove = cart.getItems().stream()
                .filter(i -> i.getBook().getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);

        if (itemToRemove != null) {
            int newQuantity = itemToRemove.getQuantity() - 1;
            if (newQuantity > 0) {
                itemToRemove.setQuantity(newQuantity);
            } else {
                cart.removeShoppingCartItem(itemToRemove);
            }
        }
    }

    public void updateBookQuantity(ShoppingCart cart, String isbn, int newQuantity) {
        ShoppingCartItem item = cart.getItems().stream()
                .filter(i -> i.getBook().getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);

        if (item != null) {
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
            } else {
                cart.removeShoppingCartItem(item);
            }
        }

    }

    public int getTotalItemCount(ShoppingCart cart) {
        return cart.getItems().stream()
                .mapToInt(ShoppingCartItem::getQuantity)
                .sum();
    }

    public boolean hasAvalibleStock(ShoppingCart cart) {
        for (ShoppingCartItem item : cart.getItems()) {
            if (!bookService.hasSufficientStock(item.getBook(), item.getQuantity())) {
                return false;
            }
        }
        return true;
    }

    public ShoppingCartItem getCartItemByBookIsbn(ShoppingCart cart, String isbn) {
        for (ShoppingCartItem item : cart.getItems()) {
            if (item.getBook().getIsbn().equals(isbn)) {
                return item;
            }
        }
        return null;
    }

    public int getQuantityOfBookInCart(ShoppingCart cart, String isbn) {
        ShoppingCartItem existingItem = getCartItemByBookIsbn(cart, isbn);
        if (existingItem != null) {
            return existingItem.getQuantity();
        }
        return 0;
    }
}
