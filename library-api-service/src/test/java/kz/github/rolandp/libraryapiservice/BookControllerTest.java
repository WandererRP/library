package kz.github.rolandp.libraryapiservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.github.rolandp.libraryapiservice.entity.Author;
import kz.github.rolandp.libraryapiservice.entity.Book;
import kz.github.rolandp.libraryapiservice.model.BookCreateRequest;
import kz.github.rolandp.libraryapiservice.model.BookUpdateRequest;
import kz.github.rolandp.libraryapiservice.repository.AuthorRepository;
import kz.github.rolandp.libraryapiservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerTest {
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(null);
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        Author newAuthor = new Author();
        newAuthor.setName("J.K. Rowling");
        newAuthor = authorRepository.save(newAuthor);
        testAuthor = newAuthor;

        Book newBook = new Book();
        newBook.setTitle("Harry Potter");
        newBook.setAuthor(testAuthor);
        testBook = bookRepository.save(newBook);
    }

    private Author testAuthor;
    private Book testBook;



    @Test
    void testAddBook() throws Exception {
        BookCreateRequest request = new BookCreateRequest("Harry Potter", testAuthor.getId());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Harry Potter"))
                .andExpect(jsonPath("$.authorName").value("J.K. Rowling"));
    }

    @Test
    void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
    @Test
    void testAddBookWithEmptyTitle() throws Exception {
        BookCreateRequest request = new BookCreateRequest("", testAuthor.getId());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetBookById() throws Exception {
        Book book = new Book();
        book.setTitle("Harry Potter");
        book.setAuthor(testAuthor);
        book = bookRepository.save(book);

        mockMvc.perform(get("/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Harry Potter"));
    }

    @Test
    void testGetNonExistingBookById() throws Exception {
        mockMvc.perform(get("/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/books/" + testBook.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    void testUpdateBook() throws Exception {
        BookUpdateRequest request = new BookUpdateRequest(testBook.getId(), "Harry Potter and the Chamber of Secrets", testAuthor.getId());

        mockMvc.perform(put("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Harry Potter and the Chamber of Secrets"));
    }

    @Test
    void testUpdateNonExistingBook() throws Exception {
        BookUpdateRequest request = new BookUpdateRequest(999L, "Non-Existent Book", testAuthor.getId());

        mockMvc.perform(put("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void testUpdateBookWithEmptyTitle() throws Exception {
        BookUpdateRequest request = new BookUpdateRequest(testBook.getId(), "", testAuthor.getId());

        mockMvc.perform(put("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}