package kz.github.rolandp.libraryapiservice.service;

import kz.github.rolandp.libraryapiservice.model.BookCreateRequest;
import kz.github.rolandp.libraryapiservice.model.BookDTO;
import kz.github.rolandp.libraryapiservice.model.BookUpdateRequest;

import java.util.List;

/**
 * @author Roland Pilpani 02.03.2025
 */
public interface BookService {
    List<BookDTO> getAllBooks();

    BookDTO getBookById(Long id);

    BookDTO addBook(BookCreateRequest bookCreateRequest);

    BookDTO updateBook(BookUpdateRequest bookUpdateRequest);

    void deleteBook(Long id);
}
