package org.example.services;

import org.example.entities.Book;
import org.example.entities.ShoppingCart;
import org.example.entities.ShoppingCartItem;
import org.example.entities.User;
import org.example.repositories.ShoppingCartRepository;
import org.example.repositories.ShoppingCartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartItemRepository itemRepository;

    public ShoppingCartService(ShoppingCartRepository cartRepository, ShoppingCartItemRepository itemRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public ShoppingCart getOrCreateCart(User user) {
        ShoppingCart cart = cartRepository.findById(user.getId());
        if (cart == null) {
            cart = new ShoppingCart(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    @Transactional
    public void addBookToCart(User user, Book book) {
        ShoppingCart cart = getOrCreateCart(user);
        ShoppingCartItem newItem = new ShoppingCartItem(cart, book);
        cart.addShoppingCartItem(newItem);
        itemRepository.save(newItem);
        cartRepository.save(cart);
    }

    @Transactional
    public void removeBookFromCart(User user, Book book) {
        ShoppingCart cart = getOrCreateCart(user);
        ShoppingCartItem item = cart.getItems().stream()
                .filter(i -> i.getBook().equals(book))
                .findFirst()
                .orElse(null);
        if (item != null) {
            cart.removeShoppingCartItem(item);
            itemRepository.save(item); // update or delete based on quantity
        }
        cartRepository.save(cart);
    }

    public double getTotal(User user) {
        return getOrCreateCart(user).getAndCalculateTotalCartPrice();
    }
}
