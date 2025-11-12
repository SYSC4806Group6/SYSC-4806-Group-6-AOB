package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.entities.Book;
import org.example.entities.ShoppingCart;
import org.example.entities.ShoppingCartItem;
import org.example.entities.User;
import org.example.services.BookCatalogService;
import org.example.services.BookService;
import org.example.services.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    private final BookCatalogService bookCatalogService;

    public ShoppingCartController(BookCatalogService bookCatalogService) {
        this.bookCatalogService = bookCatalogService;
    }

    @PostMapping("/add/{isbn}")
    @ResponseBody
    public Map<String, Object> addToCart(@PathVariable String isbn, HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }

        Book book = bookCatalogService.getBookOrThrow(isbn);

        // Check if the book already exists in the cart
        ShoppingCartItem existingItem = null;
        for (ShoppingCartItem i : cart.getItems()) {
            if (i.getBook().getIsbn().equals(isbn)) {
                existingItem = i;
                break;
            }
        }

        // If book exists increase quantity
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            ShoppingCartItem item = new ShoppingCartItem(book, 1);
            item.setShoppingCart(cart);
            cart.addShoppingCartItem(item);
        }

        // Reflect total books in UI
        int totalItems = cart.getItems().stream().mapToInt(ShoppingCartItem::getQuantity).sum();
        return Map.of("itemCount", totalItems);
    }

    @GetMapping
    public String showCart() {
        return "cart/cart";
    }

    /* To view cart (coming soon)
    @GetMapping
    public String viewCart(@SessionAttribute("loggedInUser") User user, Model model) {
        model.addAttribute("cart", cartService.getOrCreateCart(user));
        model.addAttribute("totalPrice", cartService.getTotal(user));
        return "cart/view";
    }
     */
}
