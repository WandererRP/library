package kz.github.rolandp.libraryapiservice.controller;

import jakarta.validation.Valid;
import kz.github.rolandp.libraryapiservice.model.AuthorCreateRequest;
import kz.github.rolandp.libraryapiservice.model.AuthorDTO;
import kz.github.rolandp.libraryapiservice.model.AuthorUpdateRequest;
import kz.github.rolandp.libraryapiservice.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public AuthorDTO getAuthorById(@PathVariable("id") Long id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    public AuthorDTO addAuthor(@RequestBody @Valid AuthorCreateRequest authorCreateRequest) {
        return authorService.addAuthor(authorCreateRequest);
    }

    @PutMapping
    public AuthorDTO updateAuthor(@RequestBody @Valid AuthorUpdateRequest authorUpdateRequest) {
        return authorService.updateAuthor(authorUpdateRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthor(id);
    }
}