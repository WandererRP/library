package kz.github.rolandp.libraryapiservice.model;

import kz.github.rolandp.libraryapiservice.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private Long authorId;
    private String authorName;

    public static BookDTO fromEntity(Book book) {
        return new BookDTO(
            book.getId(),
            book.getTitle(),
            book.getAuthor().getId(),
            book.getAuthor().getName()
        );
    }
}