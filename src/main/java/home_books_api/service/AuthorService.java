package home_books_api.service;

import home_books_api.model.Author;
import home_books_api.repository.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void updateAuthor(Integer id, Author newPartialAuthor) {
        this.authorRepository.findById(id).ifPresent(author -> {
            if (newPartialAuthor.getFirstName() != null) {
                author.setFirstName(
                        newPartialAuthor.getFirstName());
            }
            if (newPartialAuthor.getLastName() != null) {
                author.setLastName(newPartialAuthor.getLastName());
            }
            this.authorRepository.save(author);
        });
    }
}
