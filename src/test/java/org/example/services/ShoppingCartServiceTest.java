package org.example.services;

import org.example.entities.Book;
import org.example.entities.ShoppingCart;
import org.example.entities.ShoppingCartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class ShoppingCartServiceTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private ShoppingCart cart;
    private Book dune;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        cart = new ShoppingCart();

        dune = new Book("123", "Dune", "Desc", List.of(), null,
                "Frank Herbert", "Ace", 10.00);
        dune.setInventoryQuantity(10);
    }

    @Test
    void addBookToCart_addsNewItemIfNotPresent() {
        assertTrue(cart.getItems().isEmpty());

        shoppingCartService.addBookToCart(cart, dune);

        assertEquals(1, cart.getItems().size());
        ShoppingCartItem item = cart.getItems().get(0);
        assertEquals(1, item.getQuantity());
        assertEquals("123", item.getBook().getIsbn());
    }

    @Test
    void addBookToCart_incrementsQuantityIfExists() {
        cart.addShoppingCartItem(new ShoppingCartItem(cart, dune));

        shoppingCartService.addBookToCart(cart, dune);

        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    void removeBookFromCart_decrementsQuantity() {
        ShoppingCartItem item = new ShoppingCartItem(cart, dune);
        item.setQuantity(2);
        cart.addShoppingCartItem(item);

        shoppingCartService.removeBookFromCart(cart, "123");

        assertEquals(1, item.getQuantity());
    }

    @Test
    void removeBookFromCart_removesItemWhenQuantityHitsZero() {
        ShoppingCartItem item = new ShoppingCartItem(cart, dune);
        item.setQuantity(1);
        cart.addShoppingCartItem(item);

        shoppingCartService.removeBookFromCart(cart, "123");

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void updateBookQuantity_setsNewQuantity() {
        ShoppingCartItem item = new ShoppingCartItem(cart, dune);
        item.setQuantity(1);
        cart.addShoppingCartItem(item);

        shoppingCartService.updateBookQuantity(cart, "123", 5);

        assertEquals(5, item.getQuantity());
    }

    @Test
    void updateBookQuantity_removesItemIfZeroOrLess() {
        ShoppingCartItem item = new ShoppingCartItem(cart, dune);
        item.setQuantity(3);
        cart.addShoppingCartItem(item);

        shoppingCartService.updateBookQuantity(cart, "123", 0);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void getTotalItemCount_sumsQuantitiesCorrectly() {
        Book b1 = new Book("111", "Book1", "Desc", List.of(), null, "A", "Pub", 10);
        Book b2 = new Book("222", "Book2", "Desc", List.of(), null, "A", "Pub", 15);

        ShoppingCartItem a = new ShoppingCartItem(cart, b1);
        a.setQuantity(2);

        ShoppingCartItem b = new ShoppingCartItem(cart, b2);
        b.setQuantity(3);

        cart.addShoppingCartItem(a);
        cart.addShoppingCartItem(b);

        assertEquals(5, shoppingCartService.getTotalItemCount(cart));
    }

    @Test
    void hasAvalibleStock_returnsTrueWhenAllSufficient() {
        ShoppingCartItem item = new ShoppingCartItem(cart, dune);
        item.setQuantity(2);
        cart.addShoppingCartItem(item);

        given(bookService.hasSufficientStock(dune, 2)).willReturn(true);

        assertTrue(shoppingCartService.hasAvalibleStock(cart));
    }

    @Test
    void hasAvalibleStock_returnsFalseWhenAnyInsufficient() {
        ShoppingCartItem item = new ShoppingCartItem(cart, dune);
        item.setQuantity(5);
        cart.addShoppingCartItem(item);

        given(bookService.hasSufficientStock(dune, 5)).willReturn(false);

        assertFalse(shoppingCartService.hasAvalibleStock(cart));
    }

    @Test
    void getCartItemByBookIsbn_returnsCorrectItem() {
        ShoppingCartItem item = new ShoppingCartItem(cart, dune);
        cart.addShoppingCartItem(item);

        ShoppingCartItem result = shoppingCartService.getCartItemByBookIsbn(cart, "123");

        assertNotNull(result);
        assertEquals(item, result);
    }

    @Test
    void getCartItemByBookIsbn_returnsNullWhenNotFound() {
        assertNull(shoppingCartService.getCartItemByBookIsbn(cart, "999"));
    }

    @Test
    void getQuantityOfBookInCart_returnsCorrectQuantity() {
        ShoppingCartItem item = new ShoppingCartItem(cart, dune);
        item.setQuantity(4);
        cart.addShoppingCartItem(item);

        assertEquals(4, shoppingCartService.getQuantityOfBookInCart(cart, "123"));
    }

    @Test
    void getQuantityOfBookInCart_returnsZeroWhenMissing() {
        assertEquals(0, shoppingCartService.getQuantityOfBookInCart(cart, "nope"));
    }
}
