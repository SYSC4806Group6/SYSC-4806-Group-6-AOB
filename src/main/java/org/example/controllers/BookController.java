package org.example.controllers;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.example.entities.Book;
import org.example.entities.ShoppingCart;
import org.example.services.BookCatalogService;
import org.example.services.BookNotFoundException;
import org.example.services.BookSearchCriteria;
import org.ff4j.FF4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookController {

    private final BookCatalogService bookCatalogService;
    private final FF4j ff4j;

    public BookController(BookCatalogService bookCatalogService, FF4j ff4j) {
        this.bookCatalogService = bookCatalogService;
        this.ff4j = ff4j;
    }

    @GetMapping("/books")
    public String listBooks(@ModelAttribute("criteria") BookSearchCriteria criteria,
                            Model model,
                            HttpSession session) {

        System.out.println("ENABLE STATUS newCatalogLayout = " + ff4j.check("newCatalogLayout"));
        Page<Book> page = bookCatalogService.searchBooks(criteria);

        model.addAttribute("page", page);
        model.addAttribute("books", page.getContent());
        model.addAttribute("publishers", bookCatalogService.listPublishers());
        model.addAttribute("tags", bookCatalogService.listTags());
        model.addAttribute("sortOptions", List.of("title", "author", "price"));
        model.addAttribute("directions", Sort.Direction.values());

        //cart
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        int cartSize = cart != null ? cart.getTotalNumBooks() : 0;
        model.addAttribute("cartSize", cartSize);

        boolean baseAddToCart = ff4j.check("showAddToCart");
        boolean showAddToCart = false;

        if (baseAddToCart) {
            int percent = ff4j.getProperty("showAddToCart.rollout").asInt();

            String sessionId = session.getId();
            int hash = Math.abs(sessionId.hashCode() % 100);

            showAddToCart = (hash < percent);
        }

        model.addAttribute("showAddToCart", showAddToCart);

        boolean baseLayout = ff4j.check("newCatalogLayout");
        boolean newCatalogLayout = false;

        if (baseLayout) {
            int percent = ff4j.getProperty("newCatalogLayout.rollout").asInt();

            String sessionId = session.getId();
            int hash = Math.abs((sessionId + "layout").hashCode() % 100);

            newCatalogLayout = (hash >= percent);
        }

        model.addAttribute("newCatalogLayout", newCatalogLayout);

        return "books/list";
    }

    @GetMapping("/books/{isbn}")
    public String bookDetails(@PathVariable String isbn, Model model) {
        Book book = bookCatalogService.getBookOrThrow(isbn);
        model.addAttribute("book", book);
        return "books/details";
    }

    @ExceptionHandler(BookNotFoundException.class)
    public String handleBookNotFound(BookNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/books";
    }

}
