package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.entities.PurchaseReceipt;
import org.example.entities.ShoppingCart;
import org.example.services.PurchaseReceiptService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CheckoutController {

    private final PurchaseReceiptService purchaseReceiptService;

    public CheckoutController(PurchaseReceiptService purchaseReceiptService) {
        this.purchaseReceiptService = purchaseReceiptService;
    }

    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart/cart";
        }

        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("cartTotal", cart.getAndCalculateTotalCartPrice());

        return "/checkout/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(HttpSession session, @AuthenticationPrincipal org.example.entities.User user,
                                  @RequestParam String email,
                                  @RequestParam String name,
                                  @RequestParam String address,
                                  @RequestParam String city,
                                  @RequestParam String state,
                                  @RequestParam String zip,
                                  @RequestParam String country,
                                  Model model) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart/cart";
        }

        PurchaseReceipt receipt = purchaseReceiptService.buildReceiptFromCart(cart);
        receipt.setUser(user);
        receipt.calculateAndSetTotalCost();

        model.addAttribute("receipt", receipt);
        model.addAttribute("customerName", name);
        model.addAttribute("email", email);
        model.addAttribute("address", address + ", " + city + ", " + state + " " + zip + ", " + country);
        model.addAttribute("message", "Thank you for your order!");

        session.removeAttribute("cart");

        return "/checkout/confirmation";
    }

}