package org.example.controllers;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.example.entities.Book;
import org.example.entities.ShoppingCart;
import org.example.services.BookCatalogService;
import org.example.services.BookNotFoundException;
import org.example.services.BookSearchCriteria;
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

    public BookController(BookCatalogService bookCatalogService) {
        this.bookCatalogService = bookCatalogService;
    }

    @GetMapping("/books")
    public String listBooks(@ModelAttribute("criteria") BookSearchCriteria criteria, Model model, HttpSession session) {
        Page<Book> page = bookCatalogService.searchBooks(criteria);

        // Surface search results along with filter metadata for the Thymeleaf view.
        model.addAttribute("page", page);
        model.addAttribute("books", page.getContent());
        model.addAttribute("publishers", bookCatalogService.listPublishers());
        model.addAttribute("tags", bookCatalogService.listTags());
        model.addAttribute("sortOptions", List.of("title", "author", "price"));
        model.addAttribute("directions", Sort.Direction.values());

        ShoppingCart cart  = (ShoppingCart) session.getAttribute("cart");
        int cartSize = cart != null ? cart.getItems().size() : 0;
        model.addAttribute("cartSize", cartSize);

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
