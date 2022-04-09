package home_books_api.service;

import home_books_api.model.Author;
import home_books_api.modelDTO.AuthorDTO;
import home_books_api.repository.AuthorRepository;
import home_books_api.repository.BookRepository;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public List<AuthorDTO> getAuthorsForAngular() {
        List<Author> authors  =  this.authorRepository.findAll();
        List<AuthorDTO> authorsDTO = new ArrayList<>();
        authors.stream().forEach(author -> {
            authorsDTO.add(AuthorDTO.builder()
                    .id(author.getId())
                    .name(author.getName())
                    .noBooks((int)this.bookRepository.findByAuthorId(author.getId()).stream().count())
                    .build());
        });
        return authorsDTO;
    }

    public void updateAuthor(Integer id, Author newPartialAuthor) {
        this.authorRepository.findById(id).ifPresent(author -> {
            if (newPartialAuthor.getName() != null) {
                author.setName(
                        newPartialAuthor.getName());
            }
            this.authorRepository.save(author);
        });
    }
}
