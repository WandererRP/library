package kz.github.rolandp.libraryapiservice;

import kz.github.rolandp.libraryapiservice.entity.Author;
import kz.github.rolandp.libraryapiservice.repository.AuthorRepository;
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
class AuthorControllerTest {
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(null);
        authorRepository.deleteAll();
    }

    @Test
    void shouldCreateAuthor() throws Exception {

        String requestJson =
                """
                            {
                                "name": "Tolstoy Leo"
                            }
                        """;

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tolstoy Leo"));
    }

    @Test
    void shouldGetZeroAuthors() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldGetOneAuthor() throws Exception {
        Author author = new Author();
        author.setName("Anton Chekhov");
        author = authorRepository.save(author);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldGetAuthorById() throws Exception {
        Author author = new Author();
        author.setName("Anton Chekhov");
        author = authorRepository.save(author);

        mockMvc.perform(get("/authors/{id}", author.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(author.getId()))
                .andExpect(jsonPath("$.name").value("Anton Chekhov"));
    }

    @Test
    void shouldUpdateAuthor() throws Exception {
        Author author = new Author();
        author.setName("Anton Ch.");
        author = authorRepository.save(author);
        String updateJson =
                """
                        {
                            "id": %d,
                            "name": "Anton Chekhov"
                        }
                        """.formatted(author.getId());

        mockMvc.perform(put("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(author.getId()))
                .andExpect(jsonPath("$.name").value("Anton Chekhov"));
    }

    @Test
    void shouldDeleteAuthor() throws Exception {
        Author author = new Author();
        author.setName("Anton Chekhov");
        author = authorRepository.save(author);

        mockMvc.perform(delete("/authors/{id}", author.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/authors/{id}", author.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundForNonExistentAuthor() throws Exception {
        mockMvc.perform(get("/authors/{id}", 9999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("AUTHOR_NOT_FOUND"));
    }
}
