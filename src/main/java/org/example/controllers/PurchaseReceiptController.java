package org.example.controllers;

import org.example.entities.PurchaseReceipt;
import org.example.entities.User;
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
    public String listUserOrders(@AuthenticationPrincipal User user, //Spring Security injects the logged-in user,
                                 Model model) {

        List<PurchaseReceipt> receipts = prService.getReceiptsForUser(user);
        model.addAttribute("receipts", receipts);

        return "receipts/list";
    }

    /**
     * Displays the details for a single purchase receipt.
     */
    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable Long id,
                                   @AuthenticationPrincipal User user, //Spring Security injects the logged-in user,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        Optional<PurchaseReceipt> receiptOpt = prService.getReceiptForUser(id , user);

        if (receiptOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Order not found or no permission to view.");
            return "redirect:/purchaseReceipts"; // Redirect back to master list
        }
        model.addAttribute("receipt", receiptOpt.get());

        return "receipts/details";
    }

}