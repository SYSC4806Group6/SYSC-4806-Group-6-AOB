package org.example.services;

import org.example.entities.Book;
import org.example.entities.ShoppingCart;
import org.example.entities.ShoppingCartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private ShoppingCart cart;
    private Book book;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        // Initialize the items list to avoid null pointer if the entity default constructor isn't called
        cart.setItems(new ArrayList<>());
        book = new Book("999", "Test Book", "Desc", null, null, "Author", "Pub", 20.0);
    }

    @Test
    void addBookToCart_NewItem_AddsRow() {
        shoppingCartService.addBookToCart(cart, book);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getBook().getIsbn()).isEqualTo("999");
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(1);
    }

    @Test
    void addBookToCart_ExistingItem_IncrementsQuantity() {
        // Add it once
        shoppingCartService.addBookToCart(cart, book);
        // Add it again
        shoppingCartService.addBookToCart(cart, book);

        assertThat(cart.getItems()).hasSize(1); // Still 1 row
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(2); // Quantity increased
    }

    @Test
    void removeBookFromCart_QuantityGreaterThanOne_Decrements() {
        ShoppingCartItem item = new ShoppingCartItem(cart, book);
        item.setQuantity(2);
        cart.addShoppingCartItem(item);

        shoppingCartService.removeBookFromCart(cart, "999");

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(1);
    }

    @Test
    void removeBookFromCart_QuantityIsOne_RemovesRow() {
        ShoppingCartItem item = new ShoppingCartItem(cart, book);
        item.setQuantity(1);
        cart.addShoppingCartItem(item);

        shoppingCartService.removeBookFromCart(cart, "999");

        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    void hasAvailableStock_ReturnsTrue_WhenStockSufficient() {
        cart.addShoppingCartItem(new ShoppingCartItem(cart, book, 5));

        when(bookService.hasSufficientStock(any(Book.class), anyInt())).thenReturn(true);

        boolean result = shoppingCartService.hasAvalibleStock(cart);

        assertThat(result).isTrue();
    }
}
