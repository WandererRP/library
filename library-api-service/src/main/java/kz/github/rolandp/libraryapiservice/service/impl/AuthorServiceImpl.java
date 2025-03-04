package kz.github.rolandp.libraryapiservice.service.impl;

import kz.github.rolandp.libraryapiservice.entity.Author;
import kz.github.rolandp.libraryapiservice.exception.CustomLibraryException;
import kz.github.rolandp.libraryapiservice.model.AuthorCreateRequest;
import kz.github.rolandp.libraryapiservice.model.AuthorDTO;
import kz.github.rolandp.libraryapiservice.model.AuthorUpdateRequest;
import kz.github.rolandp.libraryapiservice.model.BookDTO;
import kz.github.rolandp.libraryapiservice.repository.AuthorRepository;
import kz.github.rolandp.libraryapiservice.repository.BookRepository;
import kz.github.rolandp.libraryapiservice.service.AuthorService;
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
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(AuthorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new CustomLibraryException(HttpStatus.NOT_FOUND, "AUTHOR_NOT_FOUND", "Author not found"));
        List<BookDTO> books = bookRepository.findByAuthorId(id)
                .stream().map(BookDTO::fromEntity)
                .collect(Collectors.toList());

        return new AuthorDTO(author.getId(), author.getName(), books);
    }

    @Override
    public AuthorDTO addAuthor(AuthorCreateRequest createRequest) {
        Author author = new Author();
        author.setName(createRequest.getName());
        author = authorRepository.save(author);

        kafkaProducerService.sendMessage("author-event-topic", "Author added: " + author.getName());
        return AuthorDTO.fromEntity(author);
    }

    @Override
    public AuthorDTO updateAuthor(AuthorUpdateRequest updateRequest) {
        Author author = authorRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new CustomLibraryException(HttpStatus.NOT_FOUND, "AUTHOR_NOT_FOUND", "Author not found"));

        author.setName(updateRequest.getName());
        author = authorRepository.save(author);

        kafkaProducerService.sendMessage("author-event-topic", "Author updated: " + author.getName());
        return AuthorDTO.fromEntity(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new CustomLibraryException(HttpStatus.NOT_FOUND, "AUTHOR_NOT_FOUND", "Author not found"));

        authorRepository.delete(author);
        kafkaProducerService.sendMessage("author-event-topic", "Author deleted: " + author.getName());
    }
}