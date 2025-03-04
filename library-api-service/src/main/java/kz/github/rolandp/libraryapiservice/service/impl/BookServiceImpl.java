package kz.github.rolandp.libraryapiservice.service.impl;

import kz.github.rolandp.libraryapiservice.entity.Author;
import kz.github.rolandp.libraryapiservice.entity.Book;
import kz.github.rolandp.libraryapiservice.exception.CustomLibraryException;
import kz.github.rolandp.libraryapiservice.model.BookCreateRequest;
import kz.github.rolandp.libraryapiservice.model.BookDTO;
import kz.github.rolandp.libraryapiservice.model.BookUpdateRequest;
import kz.github.rolandp.libraryapiservice.repository.AuthorRepository;
import kz.github.rolandp.libraryapiservice.repository.BookRepository;
import kz.github.rolandp.libraryapiservice.service.BookService;
import kz.github.rolandp.libraryapiservice.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Roland Pilpani 02.03.2025
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(BookDTO::fromEntity)
                .orElseThrow(() -> new CustomLibraryException(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", "Book not found"));
    }

    @Override
    public BookDTO addBook(BookCreateRequest bookCreateRequest) {
        Author author = authorRepository.findById(bookCreateRequest.getAuthorId())
                .orElseThrow(() -> new CustomLibraryException(HttpStatus.NOT_FOUND, "AUTHOR_NOT_FOUND", "Author not found"));

        Book book = new Book();
        book.setTitle(bookCreateRequest.getTitle());
        book.setAuthor(author);
        book = bookRepository.save(book);

        kafkaProducerService.sendMessage("book-event-topic", "Book added: " + book.getTitle() + " by " + author.getName());
        return BookDTO.fromEntity(book);
    }

    @Override
    public BookDTO updateBook(BookUpdateRequest bookUpdateRequest) {
        Book book = bookRepository.findById(bookUpdateRequest.getId())
                .orElseThrow(() -> new CustomLibraryException(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", "Book not found"));

        Author author = authorRepository.findById(bookUpdateRequest.getAuthorId())
                .orElseThrow(() -> new CustomLibraryException(HttpStatus.NOT_FOUND, "AUTHOR_NOT_FOUND", "Author not found"));

        book.setAuthor(author);
        book.setTitle(bookUpdateRequest.getTitle());
        book = bookRepository.save(book);

        kafkaProducerService.sendMessage("book-event-topic", "Book updated: " + book.getTitle() + " by " + author.getName());
        return BookDTO.fromEntity(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new CustomLibraryException(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", "Book not found"));
        bookRepository.delete(book);

        kafkaProducerService.sendMessage("book-event-topic", "Book deleted: " + book.getTitle());
    }
}