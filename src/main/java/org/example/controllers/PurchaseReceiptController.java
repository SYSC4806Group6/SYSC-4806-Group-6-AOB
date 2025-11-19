package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.entities.PurchaseReceipt;
import org.example.entities.ShoppingCart;
import org.example.entities.User;
import org.example.security.CustomUserDetails;
import org.example.services.PurchaseReceiptService;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // <-- Import this
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/purchaseReceipts")
public class PurchaseReceiptController {

    private final PurchaseReceiptService prService;

    public PurchaseReceiptController(PurchaseReceiptService prService) {
        this.prService = prService;
    }

    /**
     * Displays a list of all purchase receipts for the logged-in user.
     */
    @GetMapping
    public String listUserOrders(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, HttpSession session) {

        if (userDetails == null) {
            model.addAttribute("receipts", List.of());
            return "receipts/list";
        }

        User user = userDetails.getUser();

        List<PurchaseReceipt> receipts = prService.getReceiptsForUser(user);
        model.addAttribute("receipts", receipts);

        //Cart Icon in Header
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        int cartSize = cart != null ? cart.getTotalNumBooks() : 0;
        model.addAttribute("cartSize", cartSize);

        return "receipts/list";
    }

    /**
     * Displays the details for a single purchase receipt.
     */
    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable Long id,
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   Model model,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        User user = userDetails.getUser();
        Optional<PurchaseReceipt> receiptOpt = prService.getReceiptForUser(id, user);

        if (receiptOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Order not found or no permission to view.");
            return "redirect:/purchaseReceipts"; // Redirect back to master list
        }
        model.addAttribute("receipt", receiptOpt.get());

        //Cart Icon in Header
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        int cartSize = cart != null ? cart.getTotalNumBooks() : 0;
        model.addAttribute("cartSize", cartSize);

        return "receipts/details";
    }
}