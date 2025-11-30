package org.example.controllers;

import org.example.config.SecurityConfig;
import org.example.entities.Book;
import org.example.entities.ShoppingCart;
import org.example.entities.ShoppingCartItem;
import org.example.entities.User;
import org.example.security.CustomUserDetails;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingCartController.class)
@Import({SecurityConfig.class})
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCatalogService bookCatalogService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @MockBean
    private ShoppingCartService shoppingCartService;

    private ShoppingCart cart;
    private Book dune;

    @BeforeEach
    void setup() {
        dune = new Book("123", "Dune", "Desc", List.of(), null,
                "Frank Herbert", "Ace", 10.00);
        dune.setInventoryQuantity(5);

        ShoppingCartItem item = new ShoppingCartItem();
        item.setBook(dune);
        item.setQuantity(1);

        cart = new ShoppingCart();
        cart.addShoppingCartItem(item);
    }

    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @Test
    void viewCart_showsCartPage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", cart);

        mockMvc.perform(get("/cart").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/cart"))
                .andExpect(model().attributeExists("cartItems", "totalPrice"));
    }

    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @Test
    void addToCart_addsBookAndReturnsCount() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", new ShoppingCart());

        given(bookCatalogService.getBookOrThrow("123")).willReturn(dune);
        given(shoppingCartService.getQuantityOfBookInCart(any(), anyString()))
                .willReturn(0);
        given(bookService.hasSufficientStock(any(), anyInt()))
                .willReturn(true);
        given(shoppingCartService.getTotalItemCount(any())).willReturn(1);

        mockMvc.perform(post("/cart/add/123")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCount").value(1));
    }

    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @Test
    void removeFromCart_removesAndReturnsNewCount() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", cart);

        given(shoppingCartService.getTotalItemCount(any())).willReturn(0);

        mockMvc.perform(post("/cart/remove/123")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCount").value(0));
    }

    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @Test
    void updateCart_updatesQuantity() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", cart);

        given(shoppingCartService.getTotalItemCount(any())).willReturn(3);

        mockMvc.perform(post("/cart/update/123")
                        .param("quantity", "3")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCount").value(3));
    }

    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @Test
    void getCartTotal_returnsCorrectTotal() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", cart);

        // Let cart calculate normally
        double expected = cart.getAndCalculateTotalCartPrice();

        mockMvc.perform(get("/cart/total").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(expected));
    }
}
