package kz.github.rolandp.libraryapiservice.controller;


import jakarta.validation.Valid;
import kz.github.rolandp.libraryapiservice.model.BookCreateRequest;
import kz.github.rolandp.libraryapiservice.model.BookDTO;
import kz.github.rolandp.libraryapiservice.model.BookUpdateRequest;
import kz.github.rolandp.libraryapiservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable("id") Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public BookDTO addBook(@RequestBody @Valid BookCreateRequest bookCreateRequest) {
        return bookService.addBook(bookCreateRequest);
    }

    @PutMapping
    public BookDTO updateBook(@RequestBody @Valid BookUpdateRequest bookUpdateRequest) {
        return bookService.updateBook(bookUpdateRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
    }
}