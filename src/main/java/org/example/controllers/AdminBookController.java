package org.example.controllers;

import org.example.entities.Book;
import org.example.repositories.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    private final BookRepository bookRepository;

    public AdminBookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // List all books
    @GetMapping
    public String listBooks(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "admin/books/list";
    }

    // Show form for creating a new book
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin/books/form";
    }

    // Handle form submission for creating a new book
    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {

        if (bookRepository.existsById(book.getIsbn())) {
            Book existingBook = bookRepository.findById(book.getIsbn()).orElseThrow();

            // Manually update ONLY the editable fields
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setPublisher(book.getPublisher());
            existingBook.setPrice(book.getPrice());
            existingBook.setDescription(book.getDescription());
            existingBook.setInventoryQuantity(book.getInventoryQuantity());
            existingBook.setPictureUrl(book.getPictureUrl());

            bookRepository.save(existingBook);
            redirectAttributes.addFlashAttribute("successMessage", "Book updated successfully!");
        } else {
            bookRepository.save(book);
            redirectAttributes.addFlashAttribute("successMessage", "Book created successfully!");
        }

        return "redirect:/admin/books";
    }

    // Show form for editing an existing book
    @GetMapping("/edit/{isbn}")
    public String showEditForm(@PathVariable String isbn, Model model, RedirectAttributes redirectAttributes) {
        Book book = bookRepository.findById(isbn).orElse(null);
        if (book == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Book not found.");
            return "redirect:/admin/books";
        }
        model.addAttribute("book", book);
        return "admin/books/form";
    }

    // Delete a book
    @GetMapping("/delete/{isbn}")
    public String deleteBook(@PathVariable String isbn, RedirectAttributes redirectAttributes) {
        if (bookRepository.existsById(isbn)) {
            bookRepository.deleteById(isbn);
            redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Book not found.");
        }
        return "redirect:/admin/books";
    }
}
