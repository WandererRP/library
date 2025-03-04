package kz.github.rolandp.libraryapiservice.repository;

import kz.github.rolandp.libraryapiservice.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}