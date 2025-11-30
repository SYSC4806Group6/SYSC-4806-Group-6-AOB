package org.example.controllers;

import org.example.config.SecurityConfig;
import org.example.entities.Book;
import org.example.entities.ShoppingCart;
import org.example.entities.ShoppingCartItem;
import org.example.services.BookCatalogService;
import org.example.services.BookService;
import org.example.services.CustomUserDetailService;
import org.example.services.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingCartController.class)
@Import(SecurityConfig.class)
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @MockBean
    private BookCatalogService bookCatalogService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    private ShoppingCart cart;
    private Book book;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        book = new Book("12345", "Test Book", "Desc", new ArrayList<>(), null, "Author", "Pub", 10.0);
    }

    @Test
    @WithMockUser
    void viewCart_ReturnsCartPage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", cart);

        given(shoppingCartService.getTotalItemCount(any())).willReturn(0);

        mockMvc.perform(get("/cart").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/cart"))
                .andExpect(model().attributeExists("cartItems"));
    }

    @Test
    @WithMockUser
    void addToCart_AddsItemAndReturnsJson() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", cart);

        given(bookCatalogService.getBookOrThrow("12345")).willReturn(book);
        given(shoppingCartService.getQuantityOfBookInCart(any(), eq("12345"))).willReturn(0);
        // Assume sufficient stock
        given(bookService.hasSufficientStock(any(), anyInt())).willReturn(true);
        given(shoppingCartService.getTotalItemCount(any())).willReturn(1);

        mockMvc.perform(post("/cart/add/12345")
                        .with(csrf())
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCount").value(1));

        verify(shoppingCartService).addBookToCart(any(ShoppingCart.class), eq(book));
    }

    @Test
    @WithMockUser
    void removeFromCart_RemovesItemAndReturnsJson() throws Exception {
        MockHttpSession session = new MockHttpSession();
        cart.addShoppingCartItem(new ShoppingCartItem(cart, book));
        session.setAttribute("cart", cart);

        given(shoppingCartService.getTotalItemCount(any())).willReturn(0);

        mockMvc.perform(post("/cart/remove/12345")
                        .with(csrf())
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCount").value(0));

        verify(shoppingCartService).removeBookFromCart(any(ShoppingCart.class), eq("12345"));
    }
}