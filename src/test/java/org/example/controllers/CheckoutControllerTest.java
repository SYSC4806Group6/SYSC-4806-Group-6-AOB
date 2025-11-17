package org.example.controllers;

import org.example.entities.Book;
import org.example.services.CustomUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import org.example.config.SecurityConfig;
import org.example.entities.*;
import org.example.security.CustomUserDetails;
import org.example.services.PurchaseReceiptService;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(CheckoutController.class)
@Import({SecurityConfig.class})
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseReceiptService receiptService;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    private ShoppingCart cart;
    private CustomUserDetails userDetails;
    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("customer");

        userDetails = new CustomUserDetails(user, List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));

        Book dune = new Book("123", "Dune", "Desc", List.of(), null, "Frank Herbert", "Ace", 10.00);
        ShoppingCartItem item = new ShoppingCartItem();
        item.setBook(dune);
        item.setQuantity(2);

        cart = new ShoppingCart(user);
        cart.addShoppingCartItem(item);
    }

    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @Test
    void showCheckout_returnsCheckoutPage_whenCartExists() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", cart);

        mockMvc.perform(get("/checkout").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/checkout"))
                .andExpect(model().attributeExists("cartItems", "cartTotal"));
    }


    @Test
    void showCheckout_redirects_whenNotLogedIn() throws Exception {
        mockMvc.perform(get("/checkout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @Test
    void processCheckout_savesReceipt_andShowsConfirmation() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", cart);

        PurchaseReceipt saved = new PurchaseReceipt();
        saved.setId(99L);
        saved.setUser(user);

        given(receiptService.buildAndSaveReceiptFromCart(any(), any(), anyString(), anyString(), anyString()))
                .willReturn(saved);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

        mockMvc.perform(post("/checkout")
                        .with(authentication(auth))
                        .with(csrf())
                        .session(session)
                        .param("name", "Customer")
                        .param("email", "test@example.com")
                        .param("address", "123 Main St")
                        .param("city", "Ottawa")
                        .param("state", "ON")
                        .param("zip", "A1A1A1")
                        .param("country", "Canada"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/confirmation"))
                .andExpect(model().attributeExists("receipt"));
    }
}

