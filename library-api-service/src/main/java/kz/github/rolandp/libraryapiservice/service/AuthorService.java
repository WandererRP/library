package kz.github.rolandp.libraryapiservice.service;

import kz.github.rolandp.libraryapiservice.model.AuthorCreateRequest;
import kz.github.rolandp.libraryapiservice.model.AuthorDTO;
import kz.github.rolandp.libraryapiservice.model.AuthorUpdateRequest;

import java.util.List;

/**
 * @author Roland Pilpani 02.03.2025
 */
public interface AuthorService {
    List<AuthorDTO> getAllAuthors();

    AuthorDTO getAuthorById(Long id);

    AuthorDTO addAuthor(AuthorCreateRequest authorCreateRequest);

    AuthorDTO updateAuthor(AuthorUpdateRequest authorUpdateRequest);

    void deleteAuthor(Long id);
}
