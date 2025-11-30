package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.entities.Book;
import org.example.entities.ShoppingCart;
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
    private ShoppingCartService shoppingCartService;

    public ShoppingCartController(BookCatalogService bookCatalogService, BookService bookService) {
        this.bookCatalogService = bookCatalogService;
        this.bookService = bookService;
    }

    @PostMapping("/add/{isbn}")
    @ResponseBody
    public Map<String, Object> addToCart(@PathVariable("isbn") String isbn, HttpSession session) {

        ShoppingCart cart = getOrCreateCart(session);
        Book book = bookCatalogService.getBookOrThrow(isbn);

        int newDesiredQty = shoppingCartService.getQuantityOfBookInCart(cart, isbn) + 1;
        boolean hasStock = bookService.hasSufficientStock(book, newDesiredQty);

        boolean added = false;
        if (hasStock) {
            shoppingCartService.addBookToCart(cart, book);
            added = true;
        }

        return Map.of(
                "itemCount", shoppingCartService.getTotalItemCount(cart),
                "added", added,
                "limited", !added
        );
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {

        ShoppingCart cart = getOrCreateCart(session);

        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("totalPrice", cart.getAndCalculateTotalCartPrice());

        return "cart/cart";
    }

    @PostMapping("/remove/{isbn}")
    @ResponseBody
    public Map<String, Object> removeFromCart(@PathVariable("isbn") String isbn, HttpSession session) {

        ShoppingCart cart = getOrCreateCart(session);
        shoppingCartService.removeBookFromCart(cart, isbn);

        return Map.of("itemCount", shoppingCartService.getTotalItemCount(cart));
    }

    @PostMapping("/update/{isbn}")
    @ResponseBody
    public Map<String, Object> updateCart(
            @PathVariable("isbn") String isbn,
            @RequestParam("quantity") int quantity,
            HttpSession session) {

        ShoppingCart cart = getOrCreateCart(session);
        Book book = bookCatalogService.getBookOrThrow(isbn);

        int stock = book.getInventoryQuantity();
        int finalQuantity = Math.min(quantity, stock);
        boolean limited = quantity > stock;

        shoppingCartService.updateBookQuantity(cart, isbn, finalQuantity);

        return Map.of(
                "itemCount", shoppingCartService.getTotalItemCount(cart),
                "finalQuantity", finalQuantity,
                "limited", limited
        );
    }

    @GetMapping("/total")
    @ResponseBody
    public Map<String, Object> getCartTotal(HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        double total = (cart != null) ? cart.getAndCalculateTotalCartPrice() : 0.00;
        return Map.of("total", total);
    }

    private ShoppingCart getOrCreateCart(HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
