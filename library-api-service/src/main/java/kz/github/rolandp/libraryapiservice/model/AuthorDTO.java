package kz.github.rolandp.libraryapiservice.model;

import kz.github.rolandp.libraryapiservice.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    private Long id;
    private String name;
    private List<BookDTO> books;

    public static AuthorDTO fromEntity(Author author) {
        return new AuthorDTO(
                author.getId(),
                author.getName(),
                author.getBooks().stream()
                        .map(BookDTO::fromEntity)
                        .collect(Collectors.toList())
        );
    }
}