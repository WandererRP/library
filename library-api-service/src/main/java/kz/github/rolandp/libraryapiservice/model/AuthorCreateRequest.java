package kz.github.rolandp.libraryapiservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Roland Pilpani 03.03.2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCreateRequest {
    @NotBlank
    private String name;
}
