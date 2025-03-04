create SCHEMA if not exists library;

CREATE TABLE library.authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE library.books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author_id INT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES library.authors(id) ON DELETE CASCADE
);

INSERT INTO library.authors (name) VALUES ('J.K. Rowling'), ('George R.R. Martin');
INSERT INTO library.books (title, author_id) VALUES ('Harry Potter', 1), ('Game of Thrones', 2);