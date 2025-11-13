package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.entities.ShoppingCart;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class ViewCartController {

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }

        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("totalPrice", cart.getAndCalculateTotalCartPrice());

        return "cart";
    }
}
