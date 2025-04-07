package com.project.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    
    @Autowired
    private BookRepository bookRepository;
    
    private final BookValidator bookValidator = new BookValidator();

    @PostMapping("/addBook")
    public String addBook(@Valid @RequestBody Book book, BindingResult result) {
        bookValidator.validate(book, result);
        
        if (result.hasErrors()) {
            return "Validation failed: " + result.getAllErrors();
        }
        bookRepository.save(book);
        return "Book added successfully";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> bookData = bookRepository.findById(id);
        return bookData.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @Valid @RequestBody Book book, BindingResult result) {
        bookValidator.validate(book, result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation failed: " + result.getAllErrors());
        }
        
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        book.setId(id); // Set the id to avoid a new entity creation
        bookRepository.save(book);
        return ResponseEntity.ok("Book updated successfully");
    }
}
