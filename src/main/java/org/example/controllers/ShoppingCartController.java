package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.entities.Book;
import org.example.entities.ShoppingCart;
import org.example.entities.ShoppingCartItem;
import org.example.entities.User;
import org.example.services.BookCatalogService;
import org.example.services.BookService;
import org.example.services.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    private static final Logger log = LoggerFactory.getLogger(ShoppingCartController.class);
    private final BookCatalogService bookCatalogService;
    private final BookService bookService;

    @Autowired
    private ShoppingCartService ShoppingCartService;

    public ShoppingCartController(BookCatalogService bookCatalogService, BookService bookService) {
        this.bookCatalogService = bookCatalogService;
        this.bookService = bookService;
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

        // Don't complete the add if: stock is unavailable or already holding the potential stock in cart
        // Check the stock with the amount you currently hold + the additional one your trying to add
        boolean canAdd = bookService.hasSufficientStock(book, ShoppingCartService.getQuantityOfBookInCart(cart, isbn) + 1);

        if (canAdd) {
            ShoppingCartService.addBookToCart(cart, book);
        }

        return Map.of(
                "itemCount", ShoppingCartService.getTotalItemCount(cart),
                "added", canAdd
        );
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }

        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("totalPrice", cart.getAndCalculateTotalCartPrice());

        return "cart/cart";
    }

    @PostMapping("/remove/{isbn}")
    @ResponseBody
    public Map<String, Object> removeFromCart(@PathVariable String isbn, HttpSession session) {

        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }

        ShoppingCartService.removeBookFromCart(cart, isbn);

        session.setAttribute("cart", cart);

        int itemCount = ShoppingCartService.getTotalItemCount(cart);
        return Map.of("itemCount", itemCount);
    }

    @PostMapping("/update/{isbn}")
    @ResponseBody
    public Map<String, Object> updateCart(@PathVariable String isbn, @RequestParam int quantity, HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }

        Book book = bookCatalogService.getBookOrThrow(isbn);
        int stock = book.getInventoryQuantity();

        int finalQuantity = Math.min(quantity, stock);   // 🔥 prevents exceeding stock
        boolean limited = quantity > stock;

        ShoppingCartService.updateBookQuantity(cart, isbn, finalQuantity);

        return Map.of(
                "itemCount", ShoppingCartService.getTotalItemCount(cart),
                "finalQuantity", finalQuantity,
                "limited", limited  // 🔥 tells JS to show banner
        );
    }

    @GetMapping("/total")
    @ResponseBody
    public Map<String, Object> getCartTotal(HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        double total = (cart != null) ? cart.getAndCalculateTotalCartPrice() : 0.00;
        return Map.of("total", total);
    }
}
