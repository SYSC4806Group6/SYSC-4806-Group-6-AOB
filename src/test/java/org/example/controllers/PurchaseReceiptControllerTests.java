package org.example.controllers;

import org.example.entities.PurchaseReceipt;
import org.example.entities.ShoppingCart;
import org.example.entities.User;
import org.example.security.CustomUserDetails;
import org.example.services.CustomUserDetailService;
import org.example.services.PurchaseReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseReceiptController.class)
class PurchaseReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseReceiptService prService;

    // Needed so Spring Security can initialize properly
    @MockBean
    private CustomUserDetailService customUserDetailService;

    private CustomUserDetails userDetails;
    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("customer");

        userDetails = new CustomUserDetails(
                user,
                List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
        );
    }

    @Test
    void listUserOrders_authenticated_showsReceipts() throws Exception {
        MockHttpSession session = new MockHttpSession();

        // Cart with 3 items
        ShoppingCart cart = Mockito.mock(ShoppingCart.class);
        Mockito.when(cart.getTotalNumBooks()).thenReturn(3);
        session.setAttribute("cart", cart);

        PurchaseReceipt r1 = new PurchaseReceipt();
        r1.setId(10L);
        PurchaseReceipt r2 = new PurchaseReceipt();
        r2.setId(11L);

        given(prService.getReceiptsForUser(user)).willReturn(List.of(r1, r2));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

        mockMvc.perform(get("/purchaseReceipts")
                        .session(session)
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("receipts/list"))
                .andExpect(model().attributeExists("receipts"))
                .andExpect(model().attribute("cartSize", 3));
    }

    @Test
    void viewOrderDetails_receiptExists_displaysDetails() throws Exception {
        MockHttpSession session = new MockHttpSession();

        ShoppingCart cart = Mockito.mock(ShoppingCart.class);
        Mockito.when(cart.getTotalNumBooks()).thenReturn(2);
        session.setAttribute("cart", cart);

        PurchaseReceipt receipt = new PurchaseReceipt();
        receipt.setId(100L);

        given(prService.getReceiptForUser(100L, user)).willReturn(Optional.of(receipt));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

        mockMvc.perform(get("/purchaseReceipts/100")
                        .session(session)
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("receipts/details"))
                .andExpect(model().attribute("receipt", receipt))
                .andExpect(model().attribute("cartSize", 2));
    }

    @Test
    void viewOrderDetails_missingReceipt_redirectsWithFlash() throws Exception {
        given(prService.getReceiptForUser(anyLong(), eq(user)))
                .willReturn(Optional.empty());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

        mockMvc.perform(get("/purchaseReceipts/999")
                        .with(authentication(auth)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/purchaseReceipts"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
